/*
 * Created by Muhammad Utsman on 25/12/2018
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 12/25/18 11:47 PM
 */

package com.utsman.easygooglelogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class EasyGoogleLogin {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1213;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private LoginResultListener listener;
    private Context context;

    public EasyGoogleLogin(Context context) {
        this.context = context;
    }

    public void initGoogleLogin(String token, LoginResultListener listener) {
        this.listener = listener;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    public void initOnStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            listener.onLoginSuccess(currentUser);
        }
    }

    public void onActivityResult(Context context, int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account, context);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                listener.onLoginFailed(e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, final Context context) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog(context);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            initOnStart();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            listener.onLogoutError(task.getException());
                        }

                        hideProgressDialog();
                    }
                });
    }

    public void signIn(Context context) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut(final Context context) {
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener((Activity) context,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onLogoutSuccess(task);
                    }
                });

        mGoogleSignInClient.signOut().addOnFailureListener((Activity) context,
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onLogoutError(e);
                    }
                });
    }

    public void revokeAccess(final Context context) {
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener((Activity) context,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onLogoutSuccess(task);
                    }
                });

        mGoogleSignInClient.revokeAccess().addOnFailureListener((Activity) context,
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onLogoutError(e);
                    }
                });
    }

    private void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
