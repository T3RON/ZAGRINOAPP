package com.mseif.zagrino.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.kavenegar.sdk.excepctions.ApiException;
import com.kavenegar.sdk.excepctions.HttpException;
import com.kavenegar.sdk.models.SendResult;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Register extends AppCompatActivity {

    public String TAG = "T3RON";

    EditText edt_phone_number, edt_moaref, edt_password, edt_re_password;
    Button btn_login, btn_register;
    int randomPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_moaref = findViewById(R.id.edt_moaref);
        edt_password = findViewById(R.id.edt_password);
        edt_re_password = findViewById(R.id.edt_password);


        edt_phone_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int count_number = start + count;
                Log.d(TAG, "onTextChanged:" + count_number);
                if (count_number == 11) {
                    AndroidNetworking.post(Global.Base_Url + "Login/check_exit_phone")
                            .addBodyParameter("account_mobile", edt_phone_number.getText().toString())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("result").equals("1")) {
                                            new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("هشدار ...!")
                                                    .setContentText("این شماره قبلا به ثبت رسیده است")
                                                    .setConfirmText("بازیابی رمز عبور")
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

                }
            }
        });

        edt_moaref.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int count_number = start + count;
                Log.d(TAG, "onTextChanged:" + count_number);
                if (count_number == 11) {
                    AndroidNetworking.post(Global.Base_Url + "Login/check_exit_phone_moaref")
                            .addBodyParameter("account_mobile", edt_moaref.getText().toString())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("result").equals("1")) {
                                            new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("هشدار ...!")
                                                    .setContentText("این شماره به عنوان معرف ثبت نشده است")
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


                }
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomPIN = (int) (Math.random() * 9000) + 1000;
                Log.d(TAG, randomPIN + "");
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomPIN = (int) (Math.random() * 9000) + 1000;
                AndroidNetworking.post(Global.Base_Url + "Login/Register")
                        .addBodyParameter("account_mobile", edt_phone_number.getText().toString())
                        .addBodyParameter("account_pass", edt_password.getText().toString())
                        .addBodyParameter("account_active_code", String.valueOf(randomPIN))
                        .addBodyParameter("bazaryab_code", edt_moaref.getText().toString())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try {
                                    if (response.getString("result").equals("1")) {
                                        //Send SMS
                                        Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    try {
                                                        SendResult Result = Global.api.send("10008003007000", edt_phone_number.getText().toString(), "زاگرینو پیشرو در ارائه خدمات آنلاین - كد فعال سازی شما : " + randomPIN);
                                                        Intent i = new Intent(Register.this, Verify.class);
                                                        startActivity(i);
                                                    } catch (
                                                            HttpException ex) {
                                                        Log.i("SenderLine", ex.getMessage() + "");
                                                    } catch (ApiException ex) {
                                                        Log.i("ApiException", ex.getMessage() + "");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        thread.start();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i(TAG, e.getMessage() + "");
                                }


                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.i(TAG, error.getMessage() + "");
                            }
                        });

            }
        });

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}