package com.e.laxmibrand_admin;

import android.app.Application;
import android.os.StrictMode;




//TODO use smaller case letters for package name

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();
    private static MyApplication mInstance;
    private PreferenceSettings mPreferenceSettings;
//    private FirebaseAnalytics mFirebaseAnalytics;

    public MyApplication() {

    }

    public static synchronized MyApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public PreferenceSettings getPreferenceSettings() {
        if (mPreferenceSettings == null) {
            mPreferenceSettings = new PreferenceSettings(getApplicationContext());
        }
        return mPreferenceSettings;
    }


}
