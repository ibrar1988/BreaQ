package com.techvedika.breaq.extras;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.techvedika.breaq.R;
import com.techvedika.breaq.constant.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by ibrar on 1/1/18.
 */

public class Common {

    private static String TAG = Common.class.getSimpleName();

    public static void hideSoftKeyPad(Context context, View view) {
        InputMethodManager methodManager = (InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private static AlertDialog pAlertDialog;

    public static void alertDialog(Context ctx, String message) {

        if(!Utilities.has(ctx)) {
            return;
        }

        if(Utilities.has(pAlertDialog) && pAlertDialog.isShowing()) {

            pAlertDialog.dismiss();

            pAlertDialog = null;
        }

        pAlertDialog = new AlertDialog.Builder(ctx)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(ctx.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static Map<String,String> getHeader(Context context) {

        Map<String,String> mHeaders = new Hashtable<>();

        try {

            String serverToken = LocalStorage.getStringPreference(context, Constants.kServer_Token, null);

            if (Utilities.has(serverToken)) {
                mHeaders.put(Constants.kAuthorization, context.getResources().getString(R.string.token) + " " + serverToken);
            }

            if (Constants.isLogEnable)
                Log.d(TAG, "Authorization Token " + serverToken);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mHeaders;
    }

    private static ProgressDialog mProgressDialog;

    public static void showProgressDialog(Context context, Long timeout, String title, String message) {

        try {

            if (Utilities.has(mProgressDialog) && mProgressDialog.isShowing()) {

                mProgressDialog.dismiss();
                mProgressDialog = null;
            }

            mProgressDialog = new ProgressDialog(context);

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (Utilities.has(mProgressDialog) && mProgressDialog.isShowing()) {

                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, timeout);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (Utilities.has(mProgressDialog) && mProgressDialog.isShowing()) {

                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JSONObject parseVolleyErrorMessage(VolleyError error) {
        String errorMessage = "Network Error";
        String errorCode = "0";

        JSONObject errorObj = new JSONObject();

        if(Utilities.has(error)) {

            try {

                //java.net.SocketException: Permission denied  detailMessage

                errorCode = String.valueOf(error.networkResponse.statusCode);

                errorMessage = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {

                Log.d(TAG, "String Erro Msg :" + errorMessage);

                JSONObject jsonMsg = new JSONObject(errorMessage);

                Log.d(TAG, "JSON Erro Msg :" + jsonMsg.toString());

                JSONArray arrayMessage; // = new JSONArray();

                Log.d(TAG, "Error Message : " + errorMessage);

            } catch (JSONException je) {
                errorMessage = Constants.ErrorKeys.kDataCouldNotRead;
                je.printStackTrace();
            }
        }
        try {
            if(!errorCode.equals("0"))
            errorObj.put(Constants.ErrorKeys.kErrorCode, errorCode);
            errorObj.put(Constants.ErrorKeys.kErrorMessage, errorMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return errorObj;
    }

    public static void printVolleyErrorInDetails(VolleyError volleyError) {

        try {

            if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {

                System.out.println("volleyError getMessage()" + volleyError.getMessage());
                //OR
                System.out.println("volleyError getLocalizedMessage" + volleyError.getLocalizedMessage());
                //OR
                System.out.println("volleyError getCause()" + volleyError.getCause());
                //Or if nothing works than splitting is the only option
                System.out.println("volleyError networkResponse" + new String(volleyError.networkResponse.data));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String formatCurrency (double value) {
        return String.format("%.2f", value);
    }
}
