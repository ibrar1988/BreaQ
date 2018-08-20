package com.techvedika.breaq.extras;


/**
 * Created by ibrar on 28/12/17.
 */

public class Log {

    public static void v (String TAG, String messgage) {
        android.util.Log.v(TAG, messgage);
    }

    public static void d (String TAG, String messgage) {
        android.util.Log.d(TAG, messgage);
    }

    public static void i (String TAG, String messgage) {
        android.util.Log.i(TAG, messgage);
    }

    public static void w (String TAG, String messgage) {
        android.util.Log.w(TAG, messgage);
    }

    public static void e (String TAG, String messgage) {
        android.util.Log.e(TAG, messgage);
    }

    public static void wtf (String TAG, String messgage) {
        android.util.Log.wtf(TAG, messgage);
    }

    public static void println (int priority, String tag, String msg) {
        android.util.Log.println(priority, tag, msg);
    }
}
