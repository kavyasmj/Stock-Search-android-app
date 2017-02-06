package com.stocksearch.stocksearch;

import java.util.ArrayList;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by kavya on 5/2/2016.
 */
public class MyCustomBaseAdapter extends BaseAdapter{

    private static ArrayList<SearchResults> searchArrayList;

    private LayoutInflater mInflater;

    public MyCustomBaseAdapter(Context context, ArrayList<SearchResults> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtHeading = (TextView) convertView.findViewById(R.id.heading);
            holder.txtData = (TextView) convertView.findViewById(R.id.data);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtHeading.setText(searchArrayList.get(position).getHeading());
        String val = searchArrayList.get(position).getData();
        holder.txtData.setText(searchArrayList.get(position).getData());
//        if(position == 3 || position == 7){
        if(holder.txtHeading.getText().equals("CHANGE") || holder.txtHeading.getText().equals("CHANGEYTD")){
            if(val.contains("-")){
                holder.txtData.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.down, 0);
            }
            else{
                holder.txtData.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.up, 0);

            }
        }
        else{
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, 0, 0);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtHeading;
        TextView txtData;
    }
}
