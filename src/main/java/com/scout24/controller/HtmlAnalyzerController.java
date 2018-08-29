package com.scout24.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.common.collect.Lists;
import com.scout24.model.ErrorMessage;
import com.scout24.model.WebDocument;
import com.scout24.thread.ResourceValidation;
import com.scout24.util.HtmlParserUtil;

@Controller
public class HtmlAnalyzerController {
	
	private Logger logger = LoggerFactory.getLogger(HtmlAnalyzerController.class);

    @GetMapping("/analyzer")
    public String inputForm(Model model) {
        model.addAttribute("webDocument", new WebDocument());
        model.addAttribute("errorMsg", new ErrorMessage(""));
        return "main";
    }

    @PostMapping("/analyzer")
    public String htmlAnalyzer(@ModelAttribute WebDocument webDocument, Model model) {
    	logger.info("HTML analyzer start");
        ErrorMessage error = new ErrorMessage("");

        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        boolean isValidUrl = urlValidator.isValid(webDocument.getUri());
        if (!isValidUrl) {
            error.setDetail("Url is invalid. Please enter a valid url begin with http(s).");
            model.addAttribute("errorMsg", error);
            return "main";
        }

        try {
            Document doc = Jsoup.connect(webDocument.getUri()).get();

            List<String> hyperLinksCollection = HtmlParserUtil.getHyperLinksCollection(doc);
            int[] numOfLinks = HtmlParserUtil.getNumOfHyperMediaLink(doc, hyperLinksCollection);

            webDocument.setHtmlVersion(HtmlParserUtil.getHtmlDocType(doc));
            webDocument.setTitle(HtmlParserUtil.getTitle(doc));
            webDocument.setHeadingTagMap(HtmlParserUtil.getNumOfHeadingsByGroup(doc));
            webDocument.setNumOfInternalLinks(numOfLinks[0]);
            webDocument.setNumOfExternalLinks(numOfLinks[1]);
            webDocument.setHasLoginForm(HtmlParserUtil.hasLogin(doc));
            webDocument.setLinkResourceValidationMap(runResouceValidation(hyperLinksCollection));

        } catch (IOException e) {
        	logger.error("error while connect to URL provided "+e.getCause());
        }

        model.addAttribute("errorMsg", error);
        model.addAttribute("webDocument", webDocument);
        return "main";
    }

    /**
     * With all the links(resources) on the web document, validate if the resource is available
     *
     * @param resourceList
     * @return
     */
    public Map<String, Integer> runResouceValidation(List<String> resourceList) {

        Map<String, Integer> resourceValidationMap = new HashMap<>();

        //For performance wise, I use ExecutorService as multi-thread pool to check all the resources
        final int numOfThread = Runtime.getRuntime().availableProcessors() + 1; //thread pool size set to (No. CPU + 1) is optimal on average.
        ExecutorService threadPool = Executors.newFixedThreadPool(numOfThread);
        
        //Create a list to hold the Future object associated with Callable
        List<FutureTask<Map<String, Integer>>> futureTaskList = new ArrayList<>();

        //Use Guava Lists.partition to partition resource list into subsets.
        List<List<String>> subSets = Lists.partition(resourceList, numOfThread);

        for (List<String> subList: subSets) {
            ResourceValidation task = new ResourceValidation(subList);
            FutureTask<Map<String, Integer>> futureTask = new FutureTask<>(task);
            threadPool.submit(futureTask);
            futureTaskList.add(futureTask);
        }

        for (FutureTask<Map<String, Integer>> completeFutureTask: futureTaskList) {
            try {
                resourceValidationMap.putAll(completeFutureTask.get());
            } catch (InterruptedException e) {
            	logger.error("error while processing request "+e.getCause());
            } catch (ExecutionException e) {
            	logger.error("error while processing request "+e.getCause());
            }
        }

        threadPool.shutdown();
        return resourceValidationMap;
    }
}
