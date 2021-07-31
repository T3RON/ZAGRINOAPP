package com.mseif.zagrino.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends AppCompatActivity {

    @BindView(R.id.txt_message) TextView txt_message;
    @BindView(R.id.wifi_anim) LottieAnimationView wifi_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Tovuti.from(Splash.this).monitor(new Monitor.ConnectivityListener(){
                    @Override
                    public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
                        if (!isConnected) {
                            txt_message.setVisibility(View.VISIBLE);
                            wifi_anim.setAnimation("nocn.json");
                            wifi_anim.loop(true);
                            wifi_anim.setSpeed(1);
                            wifi_anim.playAnimation();
                            txt_message.setText("لطفا اتصال به اینترنت را بررسی کنید");
                            wifi_anim.setVisibility(View.VISIBLE);
                        }
                        else {
                            txt_message.setVisibility(View.INVISIBLE);
                            wifi_anim.setVisibility(View.VISIBLE);
                            wifi_anim.setAnimation("loading.json");
                            Intent go_main = new Intent(Splash.this,Register.class);
                            startActivity(go_main);
                            finish();
                        }

                    }
                });
            }
        }, 8000);


    }

    @Override
    protected void onStop(){
        Tovuti.from(this).stop();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}