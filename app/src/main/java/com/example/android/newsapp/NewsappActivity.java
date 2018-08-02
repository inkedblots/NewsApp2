package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class NewsappActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Newsapp>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG = NewsappActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?";

    private static final int NEWSAPP_LOADER_ID = 1;
    private ListView newsappListView;
    private NewsappAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsapp_activity);

        newsappListView = findViewById(R.id.list);
        RelativeLayout main = findViewById(R.id.main);
        mEmptyStateTextView = main.findViewById(R.id.empty_view);

        newsappListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new NewsappAdapter(this, new ArrayList<Newsapp>());
        newsappListView.setAdapter(mAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        newsappListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Newsapp currentNews = mAdapter.getItem(position);
                Uri newsappUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsappUri);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(websiteIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(websiteIntent);
                }
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
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_section_key)) ||
                key.equals(getString(R.string.settings_order_by_key))) {
            // Clear the ListView; new query started
            mAdapter.clear();
            mEmptyStateTextView.setVisibility(View.GONE);

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(NEWSAPP_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Newsapp>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String sectionId = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "0c306426-dbf2-45fe-b3ac-8d26e799c138");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("limit", "15");
        uriBuilder.appendQueryParameter("q", sectionId);
        uriBuilder.appendQueryParameter("order-by", "relevance");
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        return new NewsappLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Newsapp>> loader, List<Newsapp> newsapp) {
        // Hide loading indicator; the data loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.noArticles);
        mAdapter.clear();

        if (newsapp != null && !newsapp.isEmpty()) {
            mAdapter.addAll(newsapp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}