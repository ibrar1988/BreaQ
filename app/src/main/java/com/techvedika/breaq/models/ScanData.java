package com.techvedika.breaq.models;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;

/**
 * Created by Ibrar on 8/20/2018.
 */

public class ScanData {

    String productPrice;
    String productName;
    String materialType;

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }

    int materialCount;

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialColor() {
        return materialColor;
    }

    public void setMaterialColor(String materialColor) {
        this.materialColor = materialColor;
    }

    public String getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(String totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getChargesMessage() {
        return chargesMessage;
    }

    public void setChargesMessage(String chargesMessage) {
        this.chargesMessage = chargesMessage;
    }

    String materialColor;
    String totalItemPrice;
    String chargesMessage;

    public static ScanData fromJson(String s) {
        return new Gson().fromJson(s, ScanData.class);
    }
}
