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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Product extends AppCompatActivity {
    static final String IP = "10.0.2.2";
    String jsonObjString = "";
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // stop the keyboard from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // get itemnumber and json information
        Intent intent = getIntent();
        int itemNumber = intent.getIntExtra("itemNumber", -1);
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

