package com.techvedika.breaq.models;

import com.google.gson.Gson;

/**
 * Created by Ibrar on 8/20/2018.
 */

public class ScanData {

    double productPrice;
    String productName;
    String materialType;
    String materialColor;
    double totalItemPrice;
    String chargesMessage;
    int materialCount;

    public ScanData(double productPrice, String productName, String materialType, String materialColor, double totalItemPrice, String chargesMessage, int materialCount) {
        this.productPrice = productPrice;
        this.productName = productName;
        this.materialType = materialType;
        this.materialColor = materialColor;
        this.totalItemPrice = totalItemPrice;
        this.chargesMessage = chargesMessage;
        this.materialCount = materialCount;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
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

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getChargesMessage() {
        return chargesMessage;
    }

    public void setChargesMessage(String chargesMessage) {
        this.chargesMessage = chargesMessage;
    }

    public static ScanData fromJson(String s) {
        return new Gson().fromJson(s, ScanData.class);
    }
}
