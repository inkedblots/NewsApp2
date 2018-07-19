package com.example.android.newsapp;

public class Newsapp {

    private String mSection;

    private String mTitle;

    private String mByline;

    private String mDate;

    private String mUrl;

    Newsapp(String section, String title, String byline, String date, String url) {
        mSection = section;
        mTitle = title;
        mByline = byline;
        mDate = date;
        mUrl = url;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getByline() {
        return mByline;
    }

   public String getDate() {
        return mDate;
   }

    public String getUrl() {
        return mUrl;
    }
}
