package com.scout24.model;


import java.util.HashMap;
import java.util.Map;

public class WebDocument {

    private String uri;
    private String htmlVersion;
    private String title;
    private Map<String, Integer> headingTagMap = new HashMap<>();
    private int numOfInternalLinks;
    private int numOfExternalLinks;
    private boolean hasLoginForm;
    private Map<String, Integer> linkResourceValidationMap = new HashMap<>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHtmlVersion() {
        return htmlVersion;
    }

    public void setHtmlVersion(String htmlVersion) {
        this.htmlVersion = htmlVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Integer> getHeadingTagMap() {
        return headingTagMap;
    }

    public void setHeadingTagMap(Map<String, Integer> headingTagMap) {
        this.headingTagMap = headingTagMap;
    }

    public int getNumOfInternalLinks() {
        return numOfInternalLinks;
    }

    public void setNumOfInternalLinks(int numOfInternalLinks) {
        this.numOfInternalLinks = numOfInternalLinks;
    }

    public int getNumOfExternalLinks() {
        return numOfExternalLinks;
    }

    public void setNumOfExternalLinks(int numOfExternalLinks) {
        this.numOfExternalLinks = numOfExternalLinks;
    }

    public boolean isHasLoginForm() {
        return hasLoginForm;
    }

    public void setHasLoginForm(boolean hasLoginForm) {
        this.hasLoginForm = hasLoginForm;
    }

    public Map<String, Integer> getLinkResourceValidationMap() {
        return linkResourceValidationMap;
    }

    public void setLinkResourceValidationMap(Map<String, Integer> linkResourceValidationMap) {
        this.linkResourceValidationMap = linkResourceValidationMap;
    }
}
