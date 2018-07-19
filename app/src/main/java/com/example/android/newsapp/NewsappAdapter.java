package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        TextView sectionView = listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView bylineView = listItemView.findViewById(R.id.byline);
        bylineView.setText(currentNews.getByline());

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(currentNews.getDate());

        return listItemView;
    }
}