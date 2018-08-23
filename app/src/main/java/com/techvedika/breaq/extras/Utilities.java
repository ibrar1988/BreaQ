package com.techvedika.breaq.extras;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

/**
 * Created by ibrar on 28/12/17.
 */

public class Utilities {

    public static boolean has (Object obj) {
        return obj != null;
    }

    public static boolean has (JSONObject obj) {
        return obj != null;
    }

    public static boolean has (JSONArray obj) {
        return obj != null;
    }

    public static boolean has (String s) {
        return s != null;
    }

    public static boolean has (int value) {
        return value != 0;
    }

    public static boolean has (float value) {
        return value != 0.0;
    }

    public static boolean has (double value) {
        return value != 0.0;
    }

    public static boolean has (JSONObject json, String key) {
        if (has(json))
            if (has(key)) {
                return !json.isNull(key);
            }
        return false;
    }

    public static boolean has (ArrayList list) {
        return list != null && !list.isEmpty();
    }

    public static boolean has (Set set) {
        return set != null && !set.isEmpty();
    }

    public static String getString(Context context, int id) {
        if (has(context) && has(id))
            return context.getResources().getString(id);
        else
            return null;
    }

    public static String getCamelFormat (String input) {

        if(!Utilities.has(input) || input.equals("")) {
            return "";

        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
