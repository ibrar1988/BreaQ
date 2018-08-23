package com.techvedika.breaq.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techvedika.breaq.R;
import com.techvedika.breaq.constant.Constants;
import com.techvedika.breaq.extras.Common;
import com.techvedika.breaq.extras.CustomAlertDialog;
import com.techvedika.breaq.extras.LocalStorage;
import com.techvedika.breaq.extras.Log;
import com.techvedika.breaq.extras.Utilities;
import com.techvedika.breaq.interfaces.DialogClickInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, DialogClickInterface{

    private Context mContext;
    public static LoginActivity mLoginActivity;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvRegisterLink,tvLoginTitle;
    public TransparentProgressDialog transparent_pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mContext = this;
        mLoginActivity = this;
        init();
    }

    private void init(){
        tvLoginTitle = (TextView)findViewById(R.id.tvLoginTitle);
        String first = "Brea";
        String next = "<font color='#ff61b6'>Q</font>";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            tvLoginTitle.setText(Html.fromHtml(first+next));
        } else {
            tvLoginTitle.setText(Html.fromHtml(first+next,Html.FROM_HTML_MODE_LEGACY));
        }
        transparent_pd = new TransparentProgressDialog(mContext, "Signing In...");

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if(keyCode == KeyEvent.KEYCODE_ENTER) {

                        attemptLogin();

                        return false;
                    }
                }

                return false;
            }
        });

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.tvRegisterLink).setOnClickListener(this);
        findViewById(R.id.tvForgotPasswordLink).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        if (Utilities.has(transparent_pd) && transparent_pd.isShowing()) {
            transparent_pd.dismiss();
            transparent_pd = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
                break;
            case R.id.tvForgotPasswordLink:
                startActivity(new Intent(mContext, ForgotPasswordActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (!Utilities.has(transparent_pd) ) {
                transparent_pd = new TransparentProgressDialog(mContext, "Signing In...");
            }
            transparent_pd.show();

            JSONObject reqObj = new JSONObject();
            try {
                reqObj.put(Constants.kEmail, email);
                reqObj.put(Constants.kPassword, password);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            mPasswordView.setText("");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.kLogin_url, reqObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if(Constants.isLogEnable) {

                                Log.d(LoginActivity.class.getSimpleName(), response.toString());
                            }

                            try {

                                if (Utilities.has(transparent_pd) && transparent_pd.isShowing()) {
                                    transparent_pd.dismiss();
                                    transparent_pd = null;
                                }

                                if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {

                                    BreaQApplication.mInstance.showToast(response.getString(Constants.kStatusMessage));

                                    if(response.has("user_id")) {

                                        LocalStorage.saveStringPreference(mContext,"user_id",response.getString("user_id"));

                                        startActivity(new Intent(mContext, FranchiseActivity.class));

                                        finish();

                                    } else {
                                        CustomAlertDialog.getInstance().showCustomDialog(mContext, "Login Failed", "Sorry Couldn't received User ID on Login", "OK", null,0);
                                    }

                                } else {

                                    CustomAlertDialog.getInstance().showCustomDialog(mContext, "BreaQ", response.getString(Constants.kStatusMessage), "OK",null,0);
                                    //BreaQApplication.mInstance.showToast(response.getString(Constants.kStatusMessage));
                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mPasswordView.setText("");
                    if (Utilities.has(transparent_pd) && transparent_pd.isShowing()) {
                        transparent_pd.dismiss();
                        transparent_pd = null;
                    }
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

            BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kLogin_url);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private class TransparentProgressDialog extends Dialog {

        private ImageView imgRotator;
        private TextView tvTitle;

        private TransparentProgressDialog(Context context, String title) {
            super(context, R.style.TransparentProgressDialog);

            try {
                setCancelable(false);
                setOnCancelListener(null);
                View spinnerView = View.inflate(mContext, R.layout.signing_transaparent, null);
                tvTitle = (TextView) spinnerView.findViewById(R.id.title);
                if(Utilities.has(title) && !title.equalsIgnoreCase("")){
                    tvTitle.setText(title);
                }
                imgRotator = (ImageView) spinnerView.findViewById(R.id.signin_rotator);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                addContentView(spinnerView, params);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void show() {
            super.show();
            try {
                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(1000);
                imgRotator.setAnimation(anim);
                imgRotator.startAnimation(anim);
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public void dismissTransaparenDialog() {
        if (Utilities.has(transparent_pd) && transparent_pd.isShowing()) {
            transparent_pd.dismiss();
        }
    }

    @Override
    public void onClickPositiveButton(DialogInterface pDialog, int resultCode) {
        pDialog.dismiss();
    }

    @Override
    public void onClickNegativeButton(DialogInterface pDialog, int resultCode) {
        pDialog.dismiss();
    }
}

