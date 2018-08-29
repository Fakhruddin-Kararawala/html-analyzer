package com.scout24.util;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlParserUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HtmlParserUtil.class);

    /**
     * Extract HTML title
     *
     * @param doc
     * @return
     */
    public static String getTitle(Document doc) {
    	logger.debug("inside getTitle method");
        String title = "Title is not set";

        if (doc.title() != null) {
            title = doc.title();
        }
        return title;
    }

    /**
     * Extract HTML DOCTYPE (version)
     *
     * @param doc
     * @return
     */
    public static String getHtmlDocType(Document doc) {
    	logger.debug("inside getHtmlDocType method");
        String docType = "";

        List<Node> nods = doc.childNodes();
        for (Node node : nods) {
            if (node instanceof DocumentType) {
                DocumentType documentType = (DocumentType) node;
                docType = documentType.toString();
                break;
            }
        }
        return docType;
    }

    /**
     * Number of headings grouped by heading level
     *
     * @param doc
     * @return
     */
    public static Map<String, Integer> getNumOfHeadingsByGroup(Document doc) {
    	logger.debug("inside getNumOfHeadingsByGroup method");
        Map<String, Integer> hTagsMap = new HashMap<>();

        Elements hTags = doc.select("h1, h2, h3, h4, h5, h6");
        hTagsMap.put("h1", hTags.select("h1").size());
        hTagsMap.put("h2", hTags.select("h2").size());
        hTagsMap.put("h3", hTags.select("h3").size());
        hTagsMap.put("h4", hTags.select("h4").size());
        hTagsMap.put("h5", hTags.select("h5").size());
        hTagsMap.put("h6", hTags.select("h6").size());

        return hTagsMap;
    }

    /**
     * Get all hyperlinks from the html document
     *
     * @param doc
     * @return
     */
    public static List<String> getHyperLinksCollection(Document doc) {
    	logger.debug("inside getHyperLinksCollection method");
        List<String> hyperLinksColleciton = new ArrayList<>();
        Elements links = doc.select("a[href]");

        for (Element link : links) {
            String href = link.attr("abs:href");

            if (null != href)
                hyperLinksColleciton.add(href);
        }
        return hyperLinksColleciton;

    }

    /**
     * hypermedia links:
     * 1. get all the href tag
     * 2. check if domain match to count the number of external and internal
     * 3. with all the links, validate each link to see if it's available
     *      3.a: consider redirect
     *      3.b: reachable result will be true or false
     *      3.c: information on unreachable link
     * 4. use thread pool to generate n thread to increase performance
     *
     * @param doc
     * @param hyperlinkCollection
     * @return
     */
    public static int[] getNumOfHyperMediaLink(Document doc, List<String> hyperlinkCollection) {
    	logger.debug("inside getNumOfHyperMediaLink method");
        /**
         * use array to store the number of internal and external links
         * numOfLinks[0]: number of internal links
         * numOfLinks[1]: number of external links
         */
        int[] numOfLinks = new int[2];
        int internalLinks = 0;
        int externalLinks = 0;

        try {
            URL aURL = new URL(doc.baseUri());
            String domain = aURL.getHost();

            for (String link: hyperlinkCollection) {
                if (link.contains(domain))
                    internalLinks++;
                else
                    externalLinks++;
            }
            numOfLinks[0] = internalLinks;
            numOfLinks[1] = externalLinks;

        } catch (MalformedURLException e) {
        	logger.error("get protocol other than HTTP(s) "+doc.baseUri());
        }
        return numOfLinks;
    }

    /**
     * Check if the web document has a login form by checking the input type = password
     *
     * @param doc
     * @return
     */
    public static boolean hasLogin(Document doc) {
    	logger.debug("inside hasLogin method");
        boolean hasLoginForm = false;
        Elements inputs = doc.getElementsByTag("input");

        for (Element element : inputs) {
            String password = element.attr("type");
            if (null != password && password.equalsIgnoreCase("password")) {
                hasLoginForm = true;
                break;
            }
        }
        return hasLoginForm;
    }
}
