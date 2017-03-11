package com.example.ashleyyiu.shopsquad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ashleyyiu on 3/11/17.
 */
public class TimelineActivity extends Activity {
    // references to images
    private ArrayList<String> imageURLs = new ArrayList<String>();
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // download all images
        sendGetRequest();
        downloadImages();

        GridView gridview = (GridView) findViewById(R.id.gridView);
        Log.d("d", "Setting adapter...");

        gridview.setAdapter(new ImageAdapter(this));

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

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
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
            ImageView imageView;
            Log.d("d", "In getView...");

            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                Log.d("d", "Not null, getting images...");

                // set the length/height of the image
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int gridWidth = size.x / 3;

                imageView.setLayoutParams(new GridView.LayoutParams(gridWidth, gridWidth));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

//            imageView.setImageResource(getImageId(getApplicationContext(), Integer.toString(position)));

            return imageView;
        }
    }

    public void sendGetRequest() {

        Log.d("d", "Sending get...");

        String url = "http://10.0.2.2:3000/products";
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


                    Log.d("d", "Here are all the links");

                    for (int i = 0; i < imageURLs.size(); ++i) {
                        Log.d("d", imageURLs.get(i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("d", "cannot parse json file");
                }
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


    //        public int getImageId(Context context, String imageName) {
//            int idToReturn = context.getResources().getIdentifier("drawable/" + "product" + imageName, null, context.getPackageName());
//            Log.d("d", "id: " +idToReturn);
//            return idToReturn;
//        }
//
//
//
    public void processJson(String rawJson) throws Exception {
        Log.d("d", "Processing json:" + rawJson);

        // get the JSON array
        JSONArray list = new JSONArray(rawJson);

        // parse the array
        int i, count = list.length();

        for (i = 0; i < count; ++i) {
            // get the JSON object at specific index
            JSONObject user = list.getJSONObject(i);

            // get the product image links and add to gridView
            String imageURL = (String) user.get("picture");

            Log.d("d", "Link:" + imageURL);

            imageURLs.add(i, imageURL);

            new DownloadImage().execute(imageURL);

        }
    }

        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... URL) {
                String imageURL = URL[0];
                Bitmap bitmap = null;
                try {
                    InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("d", "Downloading image");
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                return;
            }
        }
}
