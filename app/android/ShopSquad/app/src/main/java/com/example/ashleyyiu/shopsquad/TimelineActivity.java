package com.example.ashleyyiu.shopsquad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.StrictMode;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ashleyyiu on 3/11/17.
 */
public class TimelineActivity extends Activity {

    static final String IP = "10.0.2.2";
    // references to images
    public ArrayList<String> imageURLs = new ArrayList<String>();
    Bitmap bitmap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // download all images
        queue = Volley.newRequestQueue(this);
        sendGetRequest();

        Log.d("d", "Here are all the links1");

        for (int i = 0; i < imageURLs.size(); ++i) {
            Log.d("d", imageURLs.get(i));
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Timeline Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.ashleyyiu.shopsquad/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Timeline Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.ashleyyiu.shopsquad/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
            Log.d("d", "in constructor getCount returns" + getCount());

        }

        public int getCount() {
            if (!imageURLs.isEmpty())
                return imageURLs.size();
            else return 0;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        //         create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = null;
            Log.d("d", "In getView...");

            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                Log.d("d", "Not null, getting images...");

                // set the length/height of the image
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int gridWidth = size.x / 3;

                for (int i = 0; i < imageURLs.size(); i++) {
                    Log.d("d", "IN for loop about to load images");
                    try {
                        Log.d("d", "In try");

                        URL newurl = new URL(imageURLs.get(i));

                        Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        imageView = new ImageView(mContext);

                        imageView.setLayoutParams(new GridView.LayoutParams(gridWidth, gridWidth));
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setPadding(8, 8, 8, 8);

                        imageView.setImageBitmap(bitmap);

                        imageView.setVisibility(View.VISIBLE);
                        Log.d("d", "After try");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {

                imageView = (ImageView) convertView;

//                imageView = new ImageView(mContext);
//
//                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                convertView = inflater.inflat()
            }
            return imageView;
        }
    }

    public void processJson(String rawJson) throws Exception {
        Log.d("d", "Processing json:" + rawJson);

        // get the JSON array
        JSONArray list = new JSONArray(rawJson);

        // parse the array
        int i = 0;
        int count = list.length() - 1;
        Log.d("d", "List Length:" + count);


        for (i = 0; i < count; ++i) {
            // get the JSON object at specific index
            JSONObject user = list.getJSONObject(i);

            // get the product image links and add to gridView
            String imageURL = (String) user.get("picture");
            Log.d("d", "Link:" + imageURL);
            imageURLs.add(i, imageURL.replace("localhost", IP));
        }
        Log.d("d", "There are this many links in imageURLs in parseJson:" + imageURLs.size());


    }


    public void sendGetRequest() {
        Log.d("d", "Sending get...");
        String url = "http://" + IP + ":3000/products";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // received a null response
                if (responseBody == null) {
                    Log.d("d", "responseBody is null");
                    return;
                }

                // received a valid response
                String response = new String(responseBody);
                Log.d("d", "responseBody is not null:" + response);
                Toast.makeText(getApplicationContext(), "responseBody is not null", Toast.LENGTH_LONG).show();

                try {

                    processJson(response);

                    Log.d("d", "There are this many links in imageURLs after processing Json:" + imageURLs.size());

                    Log.d("d", "Here are all the links2 ");

                    for (int i = 0; i < imageURLs.size(); ++i) {
                        Log.d("d", "linkz: " + imageURLs.get(i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("d", "cannot parse json file");
                }

                Log.d("d", "Exiting successful request");


                // create gridView here
                GridView gridview = (GridView) findViewById(R.id.gridView);

                Log.d("d", "Setting adapter...");

                gridview.setAdapter(new ImageAdapter(getBaseContext()));

                Log.d("d", "After setting adapter...");

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Log.d("d", "Clicked pic where position is " + position);

                        Intent intent = new Intent(v.getContext(), Product.class);
                        intent.putExtra("itemNumber", position);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // received a null response

                if (responseBody == null) {
                    Log.d("d", "onFailure and responseBody is null");
                    return;
                }

                // error response, do something with it!
                String response = new String(responseBody);
                Log.d("d", "Error response:" + response);

            }

        });
    }
}

//            public int getImageId(Context context, String imageName) {
//            int idToReturn = context.getResources().getIdentifier("drawable/" + "product" + imageName, null, context.getPackageName());
//            Log.d("d", "id: " +idToReturn);
//            return idToReturn;
//        }
//        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
//
//            @Override
//            protected Bitmap doInBackground(String... URL) {
//                String imageURL = URL[0];
//                Bitmap bitmap = null;
//                try {
//                    InputStream input = new java.net.URL(imageURL).openStream();
//                    bitmap = BitmapFactory.decodeStream(input);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.d("d", "Downloading image");
//                return bitmap;
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap result) {
//                return;
//            }
//        }
