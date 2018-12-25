/*
 * Created by Muhammad Utsman on 26/12/2018
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 12/25/18 11:42 PM
 */

package com.utsman.googlelogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.utsman.easygooglelogin.EasyGoogleLogin;
import com.utsman.easygooglelogin.LoginResultListener;

public class MainActivity extends AppCompatActivity implements LoginResultListener {

    private EasyGoogleLogin googleLogin;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusText = findViewById(R.id.status);

        String token = getString(R.string.default_web_client_id);
        googleLogin = new EasyGoogleLogin(this);
        googleLogin.initGoogleLogin(token, this);

        SignInButton signInButton = findViewById(R.id.sign_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin.signIn(MainActivity.this);
            }
        });

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin.signOut(MainActivity.this);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleLogin.initOnStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleLogin.onActivityResult(this, requestCode, data);
    }


    @Override
    public void onLoginSuccess(FirebaseUser user) {
        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        statusText.setText(user.getDisplayName());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoginFailed(Exception exception) {
        statusText.setText("Error login: " + exception.getLocalizedMessage());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLogoutSuccess(Task<Void> task) {
        statusText.setText("Logout Success");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLogoutError(Exception exception) {
        statusText.setText("Logout Error: "+ exception.getLocalizedMessage());
    }
}
