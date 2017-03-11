package com.example.ashleyyiu.shopsquad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // get item number
        Intent intent = getIntent();
        int itemNumber = intent.getIntExtra("itemNumber", -1);

        Log.d("d", "item num:"+itemNumber);

        // load product image
        ImageView product = (ImageView) findViewById(R.id.productImage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double imageWidthDouble = (double) (size.x/3) *2;
        int imageWidthInt = (int) imageWidthDouble;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidthInt, imageWidthInt);
        layoutParams.gravity = Gravity.LEFT;
        product.setLayoutParams(layoutParams);
        product.setScaleType(ImageView.ScaleType.CENTER_CROP);
        product.setImageResource(getImageId(this, Integer.toString(itemNumber)));

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

    public static int getImageId(Context context, String imageName) {
        int idToReturn = context.getResources().getIdentifier("drawable/" + "sample_" + imageName, null, context.getPackageName());
        Log.d("d", "id: " +idToReturn);
        return idToReturn;
    }
}

