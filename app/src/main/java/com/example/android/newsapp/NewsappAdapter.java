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
        titleView.setText(currentNews.getTitle());

        TextView sectionView = listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());

//        TextView dateView = listItemView.findViewById(R.id.date);
//        dateView.setText(currentNews.getPublicationDate());

        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentNews.getAuthor());

        if (currentNews.getAuthor() != "") {
            authorView.setText(currentNews.getAuthor());

            //Set author name view as visible
            authorView.setVisibility(View.VISIBLE);
        } else {
            //Set author name view as gone
            authorView.setVisibility(View.GONE);
        }

        // Find the TextView with view ID date
        TextView dateView = null;

        if (currentNews.getPublicationDate() != null) {
            dateView = listItemView.findViewById(R.id.date);
            // Format the date string (i.e. "Mar 3, 1984")
            String formattedDate = formatDate(currentNews.getPublicationDate());
            // Display the date of the current earthquake in that TextView
            dateView.setText(formattedDate);

            //Set date views as visible
            dateView.setVisibility(View.VISIBLE);

        } else {
            //Set date & time views as gone
            dateView.setVisibility(View.GONE);

        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
}