package com.stocksearch.stocksearch;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by kavya on 5/2/2016.
 */
// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    //--------------------------Current-----------------------------------------
    // URL to get contacts JSON
//    private static String urlString = "http://php571server-env.us-west-2.elasticbeanstalk.com/?sym=";
    private static String urlString = "http://default-environment.z6dj3knvsw.us-west-1.elasticbeanstalk.com/?sym=";
    private static String urlString2;

    private String stat;
    public static String name;
    public static String sym;
    public static String lp;
    private String ch;
    private String chp;
    public static String chpStr;
    private String ts;
    private String mc;
    private String vol;
    private String chytd;
    private String chpytd;
    private String chpytdStr;
    private String high;
    private String low;
    private String open;
    private View v;
    //--------------------------News----------------------------------------
//    private static String urlStringNews = "http://php571server-env.us-west-2.elasticbeanstalk.com/?symb=";
    private static String urlStringNews = "http://default-environment.z6dj3knvsw.us-west-1.elasticbeanstalk.com/?symb=";
    private static String urlString2News;

    private String title;
    private String url;
    private String desc;
    private String srcc;
    private String date;

    private View v3;

    private int mPage;

    PhotoViewAttacher mAttacher;
    ImageView il;
    public static String image_URL;
    ProgressDialog mProgressDialog;


    ArrayList<NewsResults> results_news = new ArrayList<NewsResults>();

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if( mPage == 1){
            v = inflater.inflate(R.layout.current, container, false);
            urlString2 ="";
            urlString2 = urlString.concat(ResultActivity.input);

            MyAsyncTask jsonAsync = new MyAsyncTask();
            jsonAsync.execute("");

            return v;
        }
        else if(mPage == 2){
            View v2 = inflater.inflate(R.layout.historical, container, false);
            final WebView myWebView = (WebView) v2.findViewById(R.id.webview);
            WebSettings webSettings = myWebView.getSettings();

            // Enable Javascript for interaction
            webSettings.setJavaScriptEnabled(true);
//            myWebView.loadUrl("http://www.google.com");

            myWebView.loadUrl("file:///android_asset/hist.html");
            final String in = ResultActivity.input.toUpperCase();;
            myWebView.setWebViewClient(new WebViewClient(){
                public void onPageFinished(WebView view, String url){
                    myWebView.loadUrl("javascript:init('"+in+"')");
                }
            });

            return v2;
        }
        else{
            v3 = inflater.inflate(R.layout.news, container, false);

            urlString2News = urlStringNews.concat(ResultActivity.input);

            MyNewsAsyncTask jsonAsyncNews = new MyNewsAsyncTask();
            jsonAsyncNews.execute("");

            return v3;
        }
    }

    private ArrayList<SearchResults> GetSearchResults(){

        ArrayList<SearchResults> results = new ArrayList<SearchResults>();

        SearchResults sr = new SearchResults();
        sr.setHeading("NAME");
        sr.setData(name);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("SYMBOL");
        sr.setData(sym);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("LASTPRICE");
        sr.setData(lp);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("CHANGE");
        sr.setData(chpStr);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("TIMESTAMP");
        sr.setData(ts);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("MARKETCAP");
        sr.setData(mc);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("VOLUME");
        sr.setData(vol);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("CHANGEYTD");
        sr.setData(chpytdStr);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("HIGH");
        sr.setData(high);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("LOW");
        sr.setData(low);
        results.add(sr);

        sr = new SearchResults();
        sr.setHeading("OPEN");
        sr.setData(open);
        results.add(sr);

        return results;
    }

//    private ArrayList<NewsResults> GetNewsResults(){
    private void GetNewsResults(){

        NewsResults nr = new NewsResults();

        nr.setTitle(title);
        nr.setStory(desc);
        nr.setPublisher(srcc);
        nr.setDate(date);
        nr.setURL(url);
        results_news.add(nr);

        return;
    }


    // The types specified here are the input data type, the progress type, and the result type
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.show();
        }

        protected String doInBackground(String... strings) {

            String jsonString = "";
            try{
                HttpURLConnection urlConnection = null;
                URL url = new URL(urlString2);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                jsonString = sb.toString();

            } catch (IOException e) {
                e.printStackTrace();}
            return jsonString;
        }

        protected void onPostExecute(String result) {

            try
            {
                JSONObject json = new JSONObject(result);

                stat        = json.getString("Status");
                name        = json.getString("Name");
                sym         = json.getString("Symbol");
                lp          = json.getString("LastPrice");
                ch          = json.getString("Change");
                chp         = json.getString("ChangePercent");
                chpStr      = json.getString("Str");
                ts          = json.getString("Timestamp");
                mc          = json.getString("MarketCap");
                vol         = json.getString("Volume");
                chytd       = json.getString("ChangeYTD");
                chpytd      = json.getString("ChangePercentYTD");
                chpytdStr   = json.getString("YTDStr");
                high        = json.getString("High");
                low         = json.getString("Low");
                open        = json.getString("Open");


                ArrayList<SearchResults> searchResults = GetSearchResults();
                ListView lv = (ListView)v.findViewById(R.id.listView);
                lv.setAdapter(new MyCustomBaseAdapter(getActivity(),searchResults));


                image_URL ="http://chart.finance.yahoo.com/t?lang=en-US&width=400&height=550&s=".concat(ResultActivity.input);

                il = new ImageView(getActivity());
                il.setClickable(true);

                il.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog builder = new Dialog(getActivity());
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        builder.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                //nothing;
                            }
                        });


                        ImageView imageView = new ImageView(getActivity());

                        imageView.setImageDrawable(il.getDrawable());
                        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        builder.show();

                        mAttacher = new PhotoViewAttacher(imageView);
                        mAttacher.update();
                    }
                });

                // Execute DownloadImage AsyncTask
                new DownloadImage().execute(image_URL);

                TextView tv1 = new TextView(getActivity());
                tv1.setText("Today's Stock Activity");
                tv1.setPadding(30, 5, 5, 5);
                lv.addFooterView(tv1);
                lv.addFooterView(il);
            }
            catch (Exception e) {}
            getActivity().setTitle(name);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            il.setImageBitmap(result);
            il.setScaleType(ImageView.ScaleType.FIT_XY);
            il.setPadding(30, 10, 30, 30);
        }
    }


    private class MyNewsAsyncTask extends AsyncTask<String, Void, String> {
//        private ProgressDialog dialog;

        protected void onPreExecute() {
//            dialog = new ProgressDialog(getActivity());
//            dialog.show();
        }

        protected String doInBackground(String... strings) {

            String jsonString = "";
            try{
                HttpURLConnection urlConnection = null;
                URL url = new URL(urlString2News);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                jsonString = sb.toString();

            } catch (IOException e) {
                e.printStackTrace();}
            return jsonString;
        }

        protected void onPostExecute(String result) {

            try
            {

                JSONArray jsonarray = new JSONArray(result);
                ListView lv = (ListView)v3.findViewById(R.id.listView2);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    title = jsonobject.getString("Title");
                    url   = jsonobject.getString("URL");
                    desc  = jsonobject.getString("Description");
                    srcc  = jsonobject.getString("Source");
                    date  = jsonobject.getString("Date");

                    GetNewsResults();
                }
                lv.setAdapter(new MyCustomNewsBaseAdapter(getActivity(),results_news));

            }
            catch (Exception e) {
                e.printStackTrace();
            }
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }

        }
    }

}

