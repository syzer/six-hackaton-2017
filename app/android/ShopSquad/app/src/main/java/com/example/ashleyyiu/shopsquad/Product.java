package com.example.ashleyyiu.shopsquad;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

public class Product extends AppCompatActivity {
    static final String IP = "172.30.7.18";
    String jsonObjString = "";
    JSONArray jsonReviews;
    JSONObject jsonObject;
    int itemNumber;
    String allReviews = "";
    private WebSocketClient webSocketClient;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        /*webSocketClient = new WebSocketClient(URI.create("wss://" + IP + "3001"), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d("ws", "connected");
            }

            @Override
            public void onMessage(String message) {
                stringMessageHandler(message);
            }

            @Override
            public void onMessage(byte[] data) {
                byteMessageHandler(data);
            }

            @Override
            public void onDisconnect(int code, String reason) {
               Log.d("ws", String.format("Disconnected with code %d, reason: %s", code, reason));
            }

            @Override
            public void onError(Exception error) {
                Log.e("ws", error.getMessage());
            }
        }, Collections.<BasicNameValuePair>emptyList());*/

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // stop the keyboard from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // get itemnumber and json information
        Intent intent = getIntent();
        itemNumber = intent.getIntExtra("itemNumber", -1);
        jsonObjString = intent.getStringExtra("product");
        Log.d("d", "item num:" + itemNumber);
        Log.d("d", "jsonObj:" + jsonObjString);

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
                Toast.makeText(getApplicationContext(), "Thank you for leaving a review! Now you can keep browsing for cool finds :)", Toast.LENGTH_LONG).show();
                reviewBox.setText("");
                Log.d("d", "Review: " + reviewText);

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

    private void stringMessageHandler(String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.com_facebook_favicon_blue)
                        .setContentTitle("New notification")
                        .setContentText(msg);
        Intent resultIntent = new Intent(this, Product.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Product.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(1, mBuilder.build());
    }

    private void byteMessageHandler(byte[] msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.com_facebook_favicon_blue)
                        .setContentTitle("New notification")
                        .setContentText(new String(msg));
        Intent resultIntent = new Intent(this, Product.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Product.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify(1, mBuilder.build());
    }

    private void getReviews() {
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

                        Log.d("d", "same?" + jsonProductId.equals(String.valueOf(itemNumber)));

                        if (jsonProductId.equals(String.valueOf(itemNumber))) {
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

                    if (allReviews.length() != 0) {
                        reviewText.setText(allReviews);
                    } else {
                        reviewText.setText("No reviews");
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
        //webSocketClient.send("getNotifications");
    }


    private void setImage() {

        // load product image
        ImageView productImage = (ImageView) findViewById(R.id.productImage);

        // set dimensions for image
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double imageWidthDouble = (double) (size.x / 3) * 2;
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

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketClient.disconnect();
    }*/
}

