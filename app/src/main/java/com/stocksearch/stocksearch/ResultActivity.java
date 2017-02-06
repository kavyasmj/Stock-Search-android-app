package com.stocksearch.stocksearch;

// Add this to the header of your file:
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * Created by kavya on 4/30/2016.
 */
public class ResultActivity extends AppCompatActivity {

    public static String input;
    private Toolbar toolbar; // Declaring the Toolbar Object

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle(PageFragment.name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            input = extras.getString("Quote");
            input = input.toUpperCase();
//            setTitle(input);
        }
        setContentView(R.layout.activity_result);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), ResultActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

     }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        MenuItem settingsItem = menu.findItem(R.id.action_favorite);

        String list ="";
        Boolean flag_fav = false;

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        if (settings.contains("FavList")) {
            list = settings.getString("FavList", "");

            String[] myData = list.split(",");
            for (String s : myData) {
                if (s.equals(input)) {
                    settingsItem.setIcon(R.drawable.ystar);
                    flag_fav = true;
                }
            }

            if(flag_fav == false){
                settingsItem.setIcon(R.drawable.bstar);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            String list ="";
            String newList ="";
            Boolean flag_fav = false;
            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
            if (settings.contains("FavList")) {
                 list = settings.getString("FavList", "");

                String[] myData = list.split(",");
                for (String s : myData) {
                    if (s.equals(input)) {
                        flag_fav = true;
                    }
                    else if(s.equals("")){
                        //do nothing
                    }
                    else{
                        newList = newList+s+',';
                    }
                }
            }

            if (!flag_fav) {
                list = list + ',' + input;
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("FavList", list);
                editor.commit();

                item.setIcon(R.drawable.ystar);
                String bm = "Bookmarked "+input;
                Toast.makeText(ResultActivity.this,bm,
                        Toast.LENGTH_LONG).show();
            }
            else{
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("FavList", newList);
                editor.commit();

                Toast.makeText(ResultActivity.this, "Removed from Favorites!",
                        Toast.LENGTH_LONG).show();
                item.setIcon(R.drawable.bstar);
            }


            return true;
        }
        else if(id == R.id.action_fb) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);
            // this part is optional
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(ResultActivity.this, "You shared this post",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(ResultActivity.this, "Did not share",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(ResultActivity.this, "Error. Did not share.",
                            Toast.LENGTH_LONG).show();
                }
            });

            String s_url = "http://chart.finance.yahoo.com/t?lang=en-US&&width=185&height=170&s="+input;
            String c_url = "http://finance.yahoo.com/q?s="+input;

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Current Stock Price of "+PageFragment.name+"is $"+PageFragment.lp)
                        .setContentDescription(
                                "Stock Information of "+PageFragment.name+" ("+PageFragment.sym+")"+
                                " LAST TRADED PRICE: $"+PageFragment.lp+", CHANGE: "+PageFragment.chpStr)
                        .setImageUrl(Uri.parse(s_url))
                        .setContentUrl(Uri.parse(c_url))
                        .build();

                shareDialog.show(linkContent);
            }
//            startActivity(new Intent(this,Facebook.class)); //??
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
