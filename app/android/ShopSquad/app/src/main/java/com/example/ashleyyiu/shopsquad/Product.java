package com.example.ashleyyiu.shopsquad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class Product extends AppCompatActivity {
    static final String IP = "10.0.2.2";
    String jsonObjString = "";
    JSONArray jsonReviews;
    JSONObject jsonObject;
    int itemNumber;
    String allReviews = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // stop the keyboard from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // get itemnumber and json information
        Intent intent = getIntent();
        itemNumber = intent.getIntExtra("itemNumber", -1);
        jsonObjString = intent.getStringExtra("product");
        Log.d("d", "item num:"+itemNumber);
        Log.d("d", "jsonObj:"+jsonObjString);

        try {
            jsonObject = new JSONObject(jsonObjString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setImage();
        setName();
        setProductReviews();

        // add event listener for submit button
        Button btn = (Button) findViewById(R.id.submitButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("d", "clicked submit");

                // extract text from review box
                EditText reviewBox = (EditText) findViewById(R.id.reviewBox);
                String reviewText = reviewBox.getText().toString();
                Toast.makeText(getApplicationContext(), "Thank you for leaving a review! Now you can keep browsing for cool finds :)" , Toast.LENGTH_LONG).show();
                reviewBox.setText("");
                Log.d("d", "Review: "+reviewText);

            }
        });

    }

    private void setName() {
        TextView nameTextView = (TextView) findViewById(R.id.productName);

        // get name from json data
        String productName = null;
        try {
            productName = (String) jsonObject.get("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nameTextView.setText(productName);

    }

    private void setProductReviews() {

        getReviews();
    }

    private void getReviews()
    {
        allReviews = "";

        Log.d("d", "Sending get for reviews...");
        String url = "http://" + IP + ":3000/reviews";
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
                    // extract json review data
                    jsonReviews = new JSONArray(response);

                    Log.d("d", "jsonReviews.length(): " + jsonReviews.length());

                    // find the corresponding entries for the item number
                    for (int i = 0; i < jsonReviews.length(); i++) {
                        String jsonProductId = jsonReviews.getJSONObject(i).getString("productId");

                        Log.d("d", "jsonProductId: " + jsonProductId);
                        Log.d("d", "itemNumber: " + String.valueOf(itemNumber));

                        Log.d("d", "same?" +jsonProductId.equals(String.valueOf(itemNumber)));

                        if(jsonProductId.equals(String.valueOf(itemNumber)))
                        {
                            Log.d("d", "Score");
                            Log.d("d", "author: " + jsonReviews.getJSONObject(i).getString("author"));
                            Log.d("d", "itemNumber: " + jsonReviews.getJSONObject(i).getString("review"));

                            allReviews += jsonReviews.getJSONObject(i).getString("author");
                            allReviews += "\n";
                            allReviews += jsonReviews.getJSONObject(i).getString("review");
                            allReviews += "\n\n";
                        }
                    }

                    TextView reviewText = (TextView) findViewById(R.id.reviews);
                    reviewText.setText(allReviews);

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


    private void setImage()
    {

        // load product image
        ImageView productImage = (ImageView) findViewById(R.id.productImage);

        // set dimensions for image
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double imageWidthDouble = (double) (size.x/3) *2;
        int imageWidthInt = (int) imageWidthDouble;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidthInt, imageWidthInt);
        layoutParams.gravity = Gravity.LEFT;

        try {

            String imageURL = (String) jsonObject.get("picture");
            URL newurl = new URL(imageURL.replace("localhost", IP));
            Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            productImage.setImageBitmap(bitmap);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        productImage.setLayoutParams(layoutParams);
        productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}

