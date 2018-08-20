package com.techvedika.breaq.models;

import com.google.gson.Gson;

/**
 * Created by Ibrar on 8/20/2018.
 */

public class ScanData {

    private String name;
    private String itemCharacteristic;
    private String itemDescription;

    public static ScanData fromJson(String s) {
        return new Gson().fromJson(s, ScanData.class);
    }
}
