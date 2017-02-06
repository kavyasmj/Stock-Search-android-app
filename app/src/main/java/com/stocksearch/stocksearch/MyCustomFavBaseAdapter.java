package com.stocksearch.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kavya on 5/2/2016.
 */
public class MyCustomFavBaseAdapter extends BaseAdapter{

    private static ArrayList<FavResults> favArrayList;

    private LayoutInflater mInflater;

//    public MyCustomFavBaseAdapter(ArrayList<FavResults> results) {
    public MyCustomFavBaseAdapter(Context context, ArrayList<FavResults> results) {
        favArrayList = results;
        mInflater = LayoutInflater.from(context);

    }

    public int getCount() {
        return favArrayList.size();
    }

    public Object getItem(int position) {
        return favArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view_favlist, null);
            holder = new ViewHolder();
            holder.txtSymbol = (TextView) convertView.findViewById(R.id.textView3);
            holder.txtLP     = (TextView) convertView.findViewById(R.id.textView4);
            holder.txtCP     = (TextView) convertView.findViewById(R.id.textView5);
            holder.txtName   = (TextView) convertView.findViewById(R.id.textView7);
            holder.txtMC     = (TextView) convertView.findViewById(R.id.textView9);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSymbol.setText(favArrayList.get(position).getSymbol());
        Linkify.addLinks( holder.txtSymbol, Linkify.WEB_URLS);
        holder.txtLP.setText(favArrayList.get(position).getLP());
        holder.txtCP.setText(favArrayList.get(position).getCP());
        holder.txtName.setText(favArrayList.get(position).getName());
        holder.txtMC.setText(favArrayList.get(position).getMC());

        String str_check = holder.txtCP.getText().toString();
        if(str_check.contains("-")){
            if(!(str_check.contains("-0.00"))){
                holder.txtCP.setBackgroundColor(Color.RED);
            }
        }
        else if(str_check .contains("+")){
            holder.txtCP.setBackgroundColor(Color.GREEN);
        }

        final String sym_check = holder.txtSymbol.getText().toString();
//        holder.txtSymbol.setOnClickListener(new View.OnClickListener() {
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("Quote",sym_check);

                Context context = v.getContext() ;
                Intent res_activity = new Intent(context, ResultActivity.class);

                //Add the bundle to the intent
                res_activity.putExtras(bundle);
                context.startActivity(res_activity);

            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView txtSymbol;
        TextView txtLP;
        TextView txtCP;
        TextView txtName;
        TextView txtMC;
    }
}
