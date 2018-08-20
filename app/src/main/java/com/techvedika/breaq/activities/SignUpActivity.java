package com.techvedika.breaq.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techvedika.breaq.R;
import com.techvedika.breaq.constant.Constants;
import com.techvedika.breaq.extras.Common;
import com.techvedika.breaq.extras.CustomAlertDialog;
import com.techvedika.breaq.extras.Log;
import com.techvedika.breaq.interfaces.DialogClickInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DialogClickInterface{

    private Context mContext;
    private EditText txt_emial_id;
    private EditText txt_pass;
    private EditText txt_confirm_pass;
    private EditText txt_first_name;
    private EditText txt_last_name;
    private EditText txt_phone_num;
    private EditText txt_city;
    private EditText txt_state;
    private LinearLayout succesSignUpLay;
    private ScrollView signUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = this;
        init();
    }

    @Override
    public void onBackPressed() {
        goToLogin();
    }

    private void init(){
        succesSignUpLay = (LinearLayout)findViewById(R.id.succesSignUpLay);
        signUpLayout = (ScrollView) findViewById(R.id.signUpLayout);
        txt_emial_id = (EditText)findViewById(R.id.txt_emial_id);
        txt_pass = (EditText)findViewById(R.id.txt_pass);
        txt_confirm_pass = (EditText)findViewById(R.id.txt_confirm_pass);
        txt_first_name = (EditText)findViewById(R.id.txt_first_name);
        txt_last_name = (EditText)findViewById(R.id.txt_last_name);
        txt_phone_num = (EditText)findViewById(R.id.txt_phone_num);
        txt_city = (EditText)findViewById(R.id.txt_city);
        txt_state = (EditText)findViewById(R.id.txt_state);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_go_login).setOnClickListener(this);
        txt_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                textColorOnPassMatch();
            }
        });

        txt_confirm_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                textColorOnPassMatch();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                goToLogin();
                break;
            case R.id.btn_register:
                doRegisterUser();
                break;
            case R.id.btn_go_login:
                goToLogin();
                break;
            default:
                break;
        }
    }

    private void goToLogin(){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    private void doRegisterUser(){
        // Validation
        String email_id = txt_emial_id.getText().toString().trim();
        String password = txt_pass.getText().toString().trim();
        String confPassword = txt_confirm_pass.getText().toString().trim();
        String first_name = txt_first_name.getText().toString().trim();
        String last_name = txt_last_name.getText().toString().trim();
        String phone_number = txt_phone_num.getText().toString().trim();
        String city = txt_city.getText().toString().trim();
        String state = txt_state.getText().toString().trim();
        if(email_id.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
            Common.alertDialog(mContext, "Please enter valid email ID");
        } else if(password.isEmpty()){
            Common.alertDialog(mContext, "Please enter password");
        }  else if(confPassword.isEmpty()){
            Common.alertDialog(mContext, "Please enter confirm password");
        } else if(!password.equals(confPassword)){
            Common.alertDialog(mContext, "Password mismatched");
        }  else if(first_name.isEmpty()){
            Common.alertDialog(mContext, "Please enter first name");
        }  else if(last_name.isEmpty()){
            Common.alertDialog(mContext, "Please enter last name");
        } else if(phone_number.isEmpty()){
            Common.alertDialog(mContext, "Please enter phone number");
        } else if(city.isEmpty()){
            Common.alertDialog(mContext, "Please enter city name");
        } else if(state.isEmpty()){
            Common.alertDialog(mContext, "Please enter state name");
        } else {

            JSONObject reqObj = new JSONObject();
            try {
                reqObj.put(Constants.kEmail,email_id);
                reqObj.put(Constants.kPassword,confPassword);
                reqObj.put(Constants.kFirstName,first_name);
                reqObj.put(Constants.kLastName,last_name);
                reqObj.put(Constants.kPhoneNumber,phone_number);
                reqObj.put(Constants.kCityName,city);
                reqObj.put(Constants.kStateName,state);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Common.showProgressDialog(mContext,Constants.DEFAULT_TIMEOUT_MS,"Signing Up", "Please wait...");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.kSignup_url, reqObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if(Constants.isLogEnable) {

                                Log.d(LoginActivity.class.getSimpleName(), response.toString());
                            }

                            Common.dismissProgressDialog();

                            try {

                                if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {

                                    //CustomAlertDialog.getInstance().showCustomDialog(mContext, "BreaQ", "You are registered successfully. Please go for login","OK",null,1);

                                    signUpLayout.setVisibility(View.GONE);
                                    succesSignUpLay.setVisibility(View.VISIBLE);

                                } else {

                                    CustomAlertDialog.getInstance().showCustomDialog(mContext, "BreaQ", response.getString(Constants.kStatusMessage),"OK",null,0);
                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Common.dismissProgressDialog();
                    Common.printVolleyErrorInDetails(error);
                    String errorMsg = "Error";
                    JSONObject errorObj = Common.parseVolleyErrorMessage(error);
                    if(errorObj.has(Constants.ErrorKeys.kErrorMessage)) {
                        try {
                            errorMsg = errorObj.getString(Constants.ErrorKeys.kErrorMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    CustomAlertDialog.getInstance().showCustomDialog(mContext, "BreaQ", errorMsg, "OK", null,0);
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, Constants.DEFAULT_MAX_RETRIES, Constants.DEFAULT_BACKOFF_MULT));

            BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kSignup_url);
        }
    }

    private void textColorOnPassMatch() {
        if(txt_pass.getText().toString().trim().equals(txt_confirm_pass.getText().toString().trim())) {
            txt_confirm_pass.setTextColor(ContextCompat.getColor(mContext,R.color.light_green));
        } else {
            txt_confirm_pass.setTextColor(ContextCompat.getColor(mContext,R.color.red));
        }
    }

    @Override
    public void onClickPositiveButton(DialogInterface pDialog, int resultCode) {
        pDialog.dismiss();
        if(resultCode==1) {
            goToLogin();
        }
    }

    @Override
    public void onClickNegativeButton(DialogInterface pDialog, int resultCode) {
        pDialog.dismiss();
    }
}
