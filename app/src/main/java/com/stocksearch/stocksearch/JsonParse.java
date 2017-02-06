package com.stocksearch.stocksearch;

/**
 * Created by kavya on 5/4/2016.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParse {

    public JsonParse(){}

    public List<SuggestGetSet> getParseJsonWCF(String sName)
    {
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        try {
            String jsonString = "";
            String intermediateString = "";

            URL js = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Lookup/jsonp/?input="+sName);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));


            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
            reader.close();

            intermediateString = sb.toString();
            jsonString = intermediateString .substring(18,intermediateString.length()-2);

            JSONArray jsonarray = new JSONArray(jsonString);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String name = jsonobject.getString("Name");
                String symbol   = jsonobject.getString("Symbol");
                String exchange  = jsonobject.getString("Exchange");

                ListData.add(new SuggestGetSet(symbol,name,exchange));

            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData;

    }
}
