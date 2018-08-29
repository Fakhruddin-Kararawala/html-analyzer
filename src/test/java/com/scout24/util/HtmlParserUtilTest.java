package com.scout24.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HtmlParserUtilTest {

    private Document documentNoLogin;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputNoLogin = new File(classLoader.getResource("test.html").getFile());
        try {
            documentNoLogin = Jsoup.parse(inputNoLogin, "UTF-8", "https://jsoup.org/cookbook/input/load-document-from-file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTitle() {
        assertEquals("failure - strings are not equal",
                HtmlParserUtil.getTitle(documentNoLogin), "Load a Document from a File: jsoup Java HTML parser");
    }

    @Test
    public void testGetHtmlDocType() {
        assertEquals("failure - strings are not equal",
                HtmlParserUtil.getHtmlDocType(documentNoLogin), "<!doctype html>");
    }

    @Test
    public void testGetNumOfHeadingsByGroup() {
        assertEquals("failure - strings are not equal",
                HtmlParserUtil.getNumOfHeadingsByGroup(documentNoLogin).toString(), "{h1=1, h2=4, h3=5, h4=1, h5=0, h6=0}");
    }

    @Test
    public void testGetHyperLinksCollection() {
        assertEquals("failure - numbers are not equal",
                HtmlParserUtil.getHyperLinksCollection(documentNoLogin).size(), 29);
    }

    @Test
    public void testGetNumOfHyperMediaLink() {
        List<String> links = HtmlParserUtil.getHyperLinksCollection(documentNoLogin);
        assertEquals("failure - numbers are not equal",
                HtmlParserUtil.getNumOfHyperMediaLink(documentNoLogin, links)[0], 28);
        assertEquals("failure - numbers are not equal",
                HtmlParserUtil.getNumOfHyperMediaLink(documentNoLogin, links)[1], 1);
    }

    @Test
    public void testHasLoginFalse() {
        assertEquals("failure - did not match",
                HtmlParserUtil.hasLogin(documentNoLogin), false);
    }
}
