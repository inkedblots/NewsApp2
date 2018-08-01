package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsappAdapter extends ArrayAdapter<Newsapp> {

    NewsappAdapter(Context context, List<Newsapp> newsapp) {
        super(context, 0, newsapp);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.newsapp_list_item, parent, false);
        }

        Newsapp currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews != null ? currentNews.getTitle() : null);

        TextView sectionView = listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews != null ? currentNews.getSection() : null);

        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentNews != null ? currentNews.getAuthor() : null);

        if ((currentNews != null ? currentNews.getAuthor() : null) != "") {
            authorView.setText(currentNews != null ? currentNews.getAuthor() : null);
            authorView.setVisibility(View.VISIBLE);
        } else {
            authorView.setVisibility(View.GONE);
        }

        TextView dateView = null;

        if (currentNews != null) {
            if (currentNews.getPublicationDate() != null) {
                dateView = listItemView.findViewById(R.id.date);
                // Format the date ("May 30, 1999")
                String formattedDate = formatDate(currentNews.getPublicationDate());
                dateView.setText(formattedDate);
                dateView.setVisibility(View.VISIBLE);
            } else {
                dateView.setVisibility(View.GONE);
            }
        }
        return listItemView;
    }

    /**
     * Return the formatted date ("May 30, 1999") from Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
}