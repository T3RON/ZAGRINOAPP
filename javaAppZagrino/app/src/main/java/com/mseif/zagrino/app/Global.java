package com.mseif.zagrino.app;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.kavenegar.sdk.KavenegarApi;

import org.json.JSONObject;

import io.kavenegar.sdk.call.KavenegarCall;
import io.kavenegar.sdk.call.enums.Environment;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Global extends Application {

    public static String Base_Url = "https://zagrino.com/json/";
    public static Context Context ;
    public static KavenegarApi api ;
    JSONObject post_response;
    public static String TAG = "kikikiki";

    @Override
    public void onCreate() {
        super.onCreate();

        Context = getApplicationContext();

        AndroidNetworking.initialize(Context);
        api = new KavenegarApi("2B467778366639745851642F4C337148556A6D317334484D6A6B566B31446F615554412F647277357562673D");




        KavenegarCall.initialize(this, Environment.PRODUCTION);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranyekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }




}
