package com.seroal.rssreader.view;

/**
 * Created by rouhalavi on 04/03/2017.
 */

public class ViewModel {
    private String title;
    private String publisher;
    private String ids;
    private String image;

    public ViewModel(String title, String publisher, String ids, String image) {
        this.title = title;
        this.publisher = publisher;
        this.ids = ids;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIds() {
        return ids;
    }

    public String getImage() {
        return image;
    }

    public String getTitlePub() {
        return title + " by " + publisher;
    }
}
