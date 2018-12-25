/*
 * Created by Muhammad Utsman on 25/12/2018
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 12/25/18 11:46 PM
 */

package com.utsman.easygooglelogin;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.utsman.easygooglelogin.test", appContext.getPackageName());
    }
}
