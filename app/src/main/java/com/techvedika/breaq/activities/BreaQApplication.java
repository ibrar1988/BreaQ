package com.techvedika.breaq.activities;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.techvedika.breaq.constant.Constants;
import com.techvedika.breaq.extras.Log;
import com.techvedika.breaq.extras.Utilities;

/**
 * Created by Ibrar on 8/2/2018.
 */

public class BreaQApplication extends Application {

    String TAG = BreaQApplication.class.getCanonicalName();

    private final long SESSION_TIME_OUT = 10*60*1000;

    private final long SESSION_INTERVAL = 1000;

    public static BreaQApplication mInstance;

    private Context context;

    private CountDownTimer sessionTimer;

    private RequestQueue mRequestQueue;

    public LocalBroadcastManager globalBroadcaster;

    @Override
    public void onCreate(){
        super.onCreate();

        // This is the entry point of this base class
        // Do whatever you want
        mInstance = this;

        context = this;

        VolleyLog.DEBUG = true;

        stopSessionTimer();
    }

    public void showToast(String message) {
        if (Utilities.has(message)) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(String message, int length) {
        if (Utilities.has(message)) {
            Toast.makeText(getApplicationContext(), message, length).show();
        }
    }

    // Creates a default instance of the worker pool

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        }

        return mRequestQueue;
    }

    // Add request to Queue with specific TAG name

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    // Add request to Queue with default TAG name

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // Cancel requests

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void initSession () {

        stopSessionTimer();

        sessionTimer = new CountDownTimer(SESSION_TIME_OUT, SESSION_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Do something on every second
            }

            @Override
            public void onFinish() {
                // Do perform task on session timeout
                if(Constants.isLogEnable)
                    Log.d(TAG, "Session timeout!!!");
                stopSessionTimer();

                // Do task on session timed out
            }
        };
        sessionTimer.start();
    }

    public void resetSessionTimer () {

        if(Utilities.has(sessionTimer)) {

            if(Constants.isLogEnable) {

                Log.d(TAG, "Session timer reset");

            }

            sessionTimer.cancel();
            sessionTimer.start();
        }
    }

    public void stopSessionTimer () {
        if(Utilities.has(sessionTimer)) {
            sessionTimer.cancel();
            sessionTimer = null;
        }
    }
}
