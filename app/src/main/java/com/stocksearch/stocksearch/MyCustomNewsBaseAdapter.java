package com.stocksearch.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kavya on 5/3/2016.
 */
public class MyCustomNewsBaseAdapter extends BaseAdapter {
    private static ArrayList<NewsResults> newsArrayList;

    private LayoutInflater mInflater;

    public MyCustomNewsBaseAdapter(Context context, ArrayList<NewsResults> results) {
        newsArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return newsArrayList.size();
    }

    public Object getItem(int position) {
        return newsArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_news_row_view, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.txtStory = (TextView) convertView.findViewById(R.id.story);
            holder.txtPublisher = (TextView) convertView.findViewById(R.id.publisher);
            holder.txtDate = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(newsArrayList.get(position).getTitle());
        Linkify.addLinks( holder.txtTitle, Linkify.WEB_URLS);
        holder.txtStory.setText(newsArrayList.get(position).getStory());
        holder.txtPublisher.setText(newsArrayList.get(position).getPublisher());
        holder.txtDate.setText(newsArrayList.get(position).getDate());

        final String url = newsArrayList.get(position).getURL();

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                v.getContext().startActivity(browserIntent);
            }
        });

        holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.txtTitle.setTextColor(Color.BLACK);
        holder.txtStory.setTypeface(null, Typeface.NORMAL);
        holder.txtPublisher.setTypeface(null, Typeface.NORMAL);
        holder.txtDate.setTypeface(null, Typeface.NORMAL);

        return convertView;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtStory;
        TextView txtPublisher;
        TextView txtDate;
    }
}
