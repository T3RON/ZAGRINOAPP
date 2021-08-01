package com.mseif.zagrino.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Verify extends AppCompatActivity {

    OtpEditText otpEditText;
    TextView countDownTextView;
    public String TAG = "T3RON";

    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        otpEditText = findViewById(R.id.otpEditText);
        countDownTextView = findViewById(R.id.countDownTextView);


        int minutes = 1;
        int milliseconds = minutes * 60 * 1000;

        countDownTimer = new CountDownTimer(milliseconds, 1000) {

            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {

                countDownTextView.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            @Override
            public void onFinish() {
                countDownTextView.setText("ارسال مجدد کد تایید");
            }
        };

        countDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.start();
            }
        });


        countDownTimer.start();

        otpEditText.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                AndroidNetworking.post(Global.Base_Url + "Login/Verify")
                        .addBodyParameter("account_active_code", value)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("result").equals("1")) {
                                        if (response.getString("active_code").equals(value)) {
                                            Global.phone_number = response.getString("active_code");
                                            Intent i = new Intent(Verify.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            new SweetAlertDialog(Verify.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("خطا")
                                                    .setContentText("کد فعال سازی اشتباه می باشد")
                                                    .setConfirmText("ارتباط با پشتیبانی")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .show();
                                        }


                                    }else {
                                        new SweetAlertDialog(Verify.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("خطا")
                                                .setContentText("سامانه با مشکل مواجه شده است")
                                                .setConfirmText("ارتباط با پشتیبانی")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d(TAG, error.getMessage() + "");
                            }
                        });
                Toast.makeText(Verify.this, "Completed " + value, Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}