package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsappLoader extends AsyncTaskLoader<List<Newsapp>> {

    private static final String LOG_TAG = NewsappLoader.class.getName();
    private String mUrl;

    NewsappLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Newsapp> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchNewsappData(mUrl);
    }
}