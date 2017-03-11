package com.example.ashleyyiu.shopsquad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

public class Login extends Activity {

    private static final String BASE_URL = "http://172.30.5.233:3000/";

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        queue = Volley.newRequestQueue(this);


        // app logo
        ImageView logo = (ImageView) findViewById(R.id.shopSquadLogo);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Collections.singletonList("user_friends"));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imageWidth = size.x / 2;


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidth, imageWidth);

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        logo.setLayoutParams(layoutParams);

        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        logo.setImageResource(R.drawable.shopsquadlogo);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String userId = loginResult.getAccessToken().getUserId();

                Log.d("login", "userId: " + userId);

                // friends list request
                getFriendsList();
            }

            @Override
            public void onCancel() {
                Log.d("login", "login cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("login", e.getMessage());
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

    private void getFriendsList() {
        final String userId = AccessToken.getCurrentAccessToken().getUserId();

        Log.d("login", "userId: " + userId);

        // friends list request
        new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/" + userId + "/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.d("login", "Friend list: " + response.getJSONObject().toString(2));
                        } catch (JSONException e) {
                            Log.e("login", e.getMessage());
                        }
                        sendLogin(userId, response.getJSONObject());
                    }
                }).executeAsync();

        Intent intent = new Intent(findViewById(R.id.login_button).getContext(), TimelineActivity.class);
        startActivity(intent);
    }

    private void sendLogin(String userId, JSONObject friends) {
        try {
            Profile p = Profile.getCurrentProfile();
            JSONObject postData = new JSONObject();
            postData.put("id", userId);
            postData.put("name", p.getName());
            postData.put("summary", friends.getJSONObject("summary"));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "logins",
                    postData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("d", "Response: " + response.toString());
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("e", error.getMessage());

                }
            });
            queue.add(request);


        } catch (Exception e) {
            Log.e("login", "error sending login info: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
