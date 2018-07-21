package com.example.android.newsapp;

import java.util.Date;

public class Newsapp {

    private String mSection;

    private String mTitle;

    private String mAuthorFullName;

    private Date mPublicationDate;

    private String mUrl;

    Newsapp(String section, String title, String authorFullName, Date pubDate, String url) {
        mSection = section;
        mTitle = title;
        mAuthorFullName = authorFullName;
        mPublicationDate = pubDate;
        mUrl = url;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthorFullName;
    }

    public Date getPublicationDate() {
        return mPublicationDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
