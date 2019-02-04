/*
 * Created by Muhammad Utsman on 25/12/2018
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 12/25/18 11:55 PM
 */

package com.utsman.easygooglelogin;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public interface LoginResultListener {
    void onLoginSuccess(FirebaseUser user);
    void onLoginFailed(Exception exception);
    void onLogoutSuccess(Task<Void> task);
    void onLogoutError(Exception exception);
}