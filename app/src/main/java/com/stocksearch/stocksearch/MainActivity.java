package com.stocksearch.stocksearch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "FavList";
    private String urlString = "http://default-environment.z6dj3knvsw.us-west-1.elasticbeanstalk.com/?sym=";
    private String urlString2;
    String symbol;


    String name;
    String sym;
    String sym1;
    String lp;
    String lp1;
    String cp1;
    String cp;
    String mc;

    String list;
    com.nhaarman.listviewanimations.itemmanipulation.DynamicListView mDynamicListView;
    private View v1;
    AutoCompleteTextView quote;
    ArrayList<FavResults> favResults = new ArrayList<FavResults>();
    MyCustomFavBaseAdapter adapter;
    Switch switchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setIcon(R.drawable.sm2);
        setContentView(R.layout.activity_main);

        mDynamicListView = (com.nhaarman.listviewanimations.itemmanipulation.DynamicListView)findViewById(R.id.dynamiclistview);
        adapter = new MyCustomFavBaseAdapter(MainActivity.this,favResults);
        mDynamicListView.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Confirmation")
                                .setMessage("Do you really want to remove from favorites?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        for (int position : reverseSortedPositions) {
                                            FavResults o1 = (FavResults)adapter.getItem(position);
                                            favResults.remove(position);
                                            adapter.notifyDataSetChanged();

                                            String list ="";
                                            String newList ="";

                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                            if (settings.contains("FavList")) {
                                                list = settings.getString("FavList", "");

                                                String[] myData = list.split(",");
                                                for (String s : myData) {
                                                    if (!s.equals(o1.getSymbol())) {
                                                        newList = newList+s+',';
                                                    }
                                                }

                                                SharedPreferences.Editor editor = settings.edit();
                                                editor.putString("FavList", newList);
                                                editor.commit();

                                                Toast.makeText(MainActivity.this, "Removed from Favorites!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                }
        );


//        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.editText);
        final AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.editText);
        acTextView.setAdapter(new SuggestionAdapter(this,acTextView.getText().toString()));

        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       TextView key = (TextView ) view;

        String s = (String) key.getText();
        acTextView.setText(s.split(" ")[0].trim());
    }
});

        switchButton = (Switch) findViewById(R.id.switch1);
        switchButton.setChecked(false);

        list ="";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.contains("FavList")) {
            list = settings.getString("FavList", "");

            if(!list.isEmpty() && !(list==null)){

                MyAsyncTask jsonAsync = new MyAsyncTask();
                jsonAsync.execute("");

            }

        }
    }

    public void getQuote(View v){
        v1 = v;
        quote   = (AutoCompleteTextView)findViewById(R.id.editText);
        symbol = quote.getText().toString();

        if(symbol.isEmpty() || symbol== null){
            displayAlertDialog("Please enter a Stock Name/Symbol");
        }
        else{
            MyAsyncTaskVal jAsync = new MyAsyncTaskVal();
                jAsync.execute("");
        }
    }

    public void onClear(View v){
        quote = (AutoCompleteTextView)findViewById(R.id.editText);
        quote.setText("");
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {

            String jsonString = "";

            String[] myData = list.split(",");
            for (String s : myData) {

                if(s.isEmpty()||s==null){
                    continue;
                }
                urlString2 ="";
                urlString2 = urlString.concat(s);


            jsonString = "";
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

                try
                {
                    JSONObject json = new JSONObject(jsonString);
                    name        = json.getString("Name");
                    sym         = json.getString("Symbol");
                    lp          = json.getString("LastPrice");
                    cp          = json.getString("ChangePercent");
                    mc          = json.getString("MarketCap"); ;

                    GetFavResults();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();}

                name="";
                sym="";
                lp="";
                cp="";
                mc="";

            }
            return jsonString;

        }

        protected void onPostExecute(String result) {
            mDynamicListView.setAdapter(adapter);
        }
    }

    private ArrayList<FavResults> GetFavResults(){

        FavResults fr = new FavResults();
        fr.setSymbol(sym);
        fr.setLP(lp);
        fr.setCP(cp);
        fr.setName(name);
        fr.setMC(mc);
        favResults.add(fr);

        return favResults;
    }

    // The types specified here are the input data type, the progress type, and the result type
    private class MyAsyncTaskVal extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            String jsonString = "";
            try{
                URL js = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol="+symbol);
                URLConnection jc = js.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                reader.close();

                jsonString = sb.toString();

            } catch (IOException e) {
                e.printStackTrace();}
            return jsonString;
        }

        protected void onPostExecute(String result) {
            try
            {
                String stat="";
                JSONObject json = new JSONObject(result);
                stat = json.getString("Status");
                if(!stat.equals("SUCCESS")){
                    displayAlertDialog("Invalid Symbol");
                }
                else{
                    if(v1.getId() == R.id.button2){
                        //Create the bundle
                        Bundle bundle = new Bundle();
                        //Add your data to bundle
                        bundle.putString("Quote",quote.getText().toString());

                        Intent res_activity = new Intent(MainActivity.this, ResultActivity.class);

                        //Add the bundle to the intent
                        res_activity.putExtras(bundle);
                        startActivity(res_activity);
                    }
                }
            }
            catch (Exception e) {
                displayAlertDialog("Invalid Symbol");
            }
        }
    }


    public void displayAlertDialog(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    public void refresh_fav_list(View v){

        MyAsyncTaskUpdate jAsync = new MyAsyncTaskUpdate();
        jAsync.execute("");

    }


    private void runThread(final View v) {

         new Thread() {
            public void run() {
                int i=0;
                while (i++ < 1000) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyAsyncTaskUpdate5 jAsync = new MyAsyncTaskUpdate5();
                                jAsync.execute("");
                            }
                        });
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void refresh_fav_list_five(final View v){

        if(switchButton.isChecked()){

            runThread(v);

        }
        else{
            try{
                Thread.interrupted();
            }catch(Exception e){System.out.println("Exception handled "+e);}


        }
    }

    private class MyAsyncTaskUpdate  extends AsyncTask<String, Void, String> {
                private ProgressDialog dialog;
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
                dialog.show();
        }

        protected String doInBackground(String... strings) {

            String jsonString = "";

            String[] myData = list.split(",");
            int i=0;
            for (String s : myData) {
                if(s.isEmpty()||s==null){
                    continue;
                }
                urlString2 ="";
                urlString2 = urlString.concat(s);


                jsonString = "";
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

                    try
                    {
                        JSONObject json = new JSONObject(jsonString);
                        sym1 = json.getString("Symbol");
                        lp1  = json.getString("LastPrice");
                        cp1  = json.getString("ChangePercent");

                        FavResults o1 = (FavResults) adapter.getItem(i);
                        o1.setLP(lp1);
                        o1.setCP(cp1);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();}

                sym1="";
                lp1="";
                cp1="";
                i++;
            }
            return jsonString;

        }

        protected void onPostExecute(String result) {

            adapter.notifyDataSetChanged();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    private class MyAsyncTaskUpdate5  extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);

            if(switchButton.isChecked()) {
                dialog.show();
            }
            else{
                dialog.hide();
            }
        }

        protected String doInBackground(String... strings) {

            String jsonString = "";

            String[] myData = list.split(",");
            int i=0;
            for (String s : myData) {
                if(s.isEmpty()||s==null){
                    continue;
                }
                urlString2 ="";
                urlString2 = urlString.concat(s);


                jsonString = "";
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

                    try
                    {
                        JSONObject json = new JSONObject(jsonString);
                        sym1         = json.getString("Symbol");
                        lp1          = json.getString("LastPrice");
                        cp1          = json.getString("ChangePercent");


                        FavResults o1 = (FavResults) adapter.getItem(i);
                        o1.setLP(lp1);
                        o1.setCP(cp1);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();}

                sym1="";
                lp1="";
                cp1="";
                i++;
            }
            return jsonString;

        }

        protected void onPostExecute(String result) {

            adapter.notifyDataSetChanged();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }


}
