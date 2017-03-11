package com.example.ashleyyiu.shopsquad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONObject;

public class Login extends Activity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private LoginResult loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        

        // app logo
        ImageView logo = (ImageView) findViewById(R.id.shopSquadLogo);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imageWidth = size.x/2;


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidth, imageWidth);

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        logo.setLayoutParams(layoutParams);

        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        logo.setImageResource(R.drawable.shopsquadlogo);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult = loginResult;
                // friends list request
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // store friends list on server for the given
                                // loginResult.getAccessToken().getUserId()
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
                //Intent intent = new Intent( ca.getContext(), TimelineActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        // fake button
        Button btn = (Button) findViewById(R.id.fakebutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("d", "clicked");
                Intent intent = new Intent(v.getContext(), TimelineActivity.class);
                startActivity(intent);
            }
        });

    }
}
