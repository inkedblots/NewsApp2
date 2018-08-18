package com.example.android.newsapp;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A whole host of individuals assisted me with this this project, as well as stack overflow,
 * Google search, comments on Slack and chats on several social media channels. Many code snippets
 * were not of my creation. If it was not for the help of these individuals I wouldn't have
 * gotten this far. I thank them whole heartedly.
 * Here are the people from Udacity that assisted: Matthew Bailey (@TheBaileyBrew),
 * Charles Rowland, Chris Addington (Chris A(ND)ddington), Erin Banister (@Erin), Olivia Meiring,
 * and Lori [ABND]
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Newsapp> fetchNewsappData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request.", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newsapp Json results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    @SuppressLint("SimpleDateFormat")
    private static List<Newsapp> extractFeatureFromJson(String newsappJSON) {
        if (TextUtils.isEmpty(newsappJSON)) {
            return null;
        }

        List<Newsapp> newsapp = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsappJSON);
            JSONObject newsJSONObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsappArray = newsJSONObject.getJSONArray("results");

            for (int i = 0; i < newsappArray.length(); i++) {

                JSONObject currentNews = newsappArray.getJSONObject(i);

                String title = currentNews.getString("webTitle");
                String sectionId = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");
                JSONArray tags = currentNews.getJSONArray("tags");

                //If "tags" array is not null
                String authorFullName = "";
                if (!tags.isNull(0)) {
                    JSONObject currentTag = tags.getJSONObject(0);

                    //Author first name
                    String authorFirstName = !currentTag.isNull("firstName") ? currentTag.getString("firstName") : "";

                    //Author last name
                    String authorLastName = !currentTag.isNull("lastName") ? currentTag.getString("lastName") : "";

                    //Author full name
                    authorFullName = StringUtils.capitalize(authorFirstName.toLowerCase().trim()) + " " + StringUtils.capitalize(authorLastName.toLowerCase().trim());
                    if (authorFirstName.trim() != "" || authorLastName.trim() != "") {
                        authorFullName = ("by ").concat(authorFullName);
                    } else {
                        authorFullName = "";
                    }
                }

                // Extract the value for the key called "webPublicationDate"
                String origPublicationDate = currentNews.getString("webPublicationDate");

                //Format publication date
                Date publicationDate = null;
                try {
                    publicationDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(origPublicationDate);
                } catch (Exception e) {
                    // If an error is thrown in the "try" block, catch the exception here,
                    // Print a log message with the message from the exception.
                    Log.e("QueryUtils", "Problem parsing the news date", e);
                }

                Newsapp newsapps = new Newsapp(sectionId, title, authorFullName, publicationDate, url);
                newsapp.add(newsapps);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the newsapp JSON results", e);
        }
        return newsapp;
    }
}