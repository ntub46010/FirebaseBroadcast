package com.vincent.firebroadcast.broadcast_helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.vincent.firebroadcast.broadcast_helper.manager.FirebaseUserManager;

public class PSNApplication extends Application {
    private static Context APPLICATION;
    private static Resources RESOURCE;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initData();
    }

    public static Context getAPPLICATION() {
        return APPLICATION;
    }

    public static Resources getRESOURCE() {
        return RESOURCE;
    }

    private void initData() {
        APPLICATION = this;
        RESOURCE = this.getResources();
        FirebaseUserManager.initialize(this.getApplicationContext());
    }
}
