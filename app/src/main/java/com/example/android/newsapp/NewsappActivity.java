package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsappActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Newsapp>> {

    public static final String LOG_TAG = NewsappActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?from-date=2018-06-01&to-date=2018-08-01&show-tags=contributor&q=music&api-key=0c306426-dbf2-45fe-b3ac-8d26e799c138";

    private static final int NEWSAPP_LOADER_ID = 1;
    ListView newsappListView;
    private NewsappAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsapp_activity);

        newsappListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsappListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsappAdapter(this, new ArrayList<Newsapp>());

        newsappListView.setAdapter(mAdapter);

        newsappListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Newsapp currentNews = mAdapter.getItem(position);
                Uri newsappUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsappUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWSAPP_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.noInternet);
        }
    }

    @Override
    public Loader<List<Newsapp>> onCreateLoader(int i, Bundle bundle) {
        return new NewsappLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Newsapp>> loader, List<Newsapp> newsapp) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.noArticles);

        if (newsapp != null && !newsapp.isEmpty()) {
            updateUi(newsapp);
        }
    }

    private void updateUi(List<Newsapp> newsapp) {
        mEmptyStateTextView.setVisibility(View.GONE);
        mAdapter.clear();
        mAdapter = new NewsappAdapter(this, newsapp);
        newsappListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Newsapp>> loader) {
        mAdapter.clear();
    }
}