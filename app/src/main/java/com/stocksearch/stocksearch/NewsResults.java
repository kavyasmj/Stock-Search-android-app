package com.stocksearch.stocksearch;

/**
 * Created by kavya on 5/3/2016.
 */
public class NewsResults {
    private String title = "";
    private String story = "";
    private String publisher = "";
    private String date = "";
    private String url = "";

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setStory(String story) {
        this.story = story;
    }

    public String getStory() {
        return story;
    }
    public void setPublisher(String publisher) {
        this.publisher = "Publisher : "+publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setDate(String date) {
        this.date = "Date : "+date;
    }

    public String getDate() {
        return date;
    }
    public void setURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }

}
