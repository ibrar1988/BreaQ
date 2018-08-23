package com.techvedika.breaq.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.vision.barcode.Barcode;
import com.techvedika.breaq.R;
import com.techvedika.breaq.constant.Constants;
import com.techvedika.breaq.extras.Common;
import com.techvedika.breaq.extras.CustomAlertDialog;
import com.techvedika.breaq.extras.LocalStorage;
import com.techvedika.breaq.extras.Log;
import com.techvedika.breaq.extras.Utilities;
import com.techvedika.breaq.models.ScanData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class QRScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private Context mContext;
    private BarcodeReader barcodeReader;
    private String isFrom;
    private JSONObject response = null;
    private String session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        this.mContext = this;
        Intent intentData = getIntent();
        isFrom = intentData.getStringExtra("isFrom");
        getSessionID();
        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onBackPressed() {
        if(Utilities.has(isFrom)) {
            if(isFrom.equalsIgnoreCase(ProductScanActivity.class.getSimpleName())) {
                startActivity(new Intent(mContext, ProductScanActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();
        // ticket details activity by passing barcode

        JSONObject reqObj = new JSONObject();

        if(Utilities.has(session_id)) {

            try {
                reqObj.put("session_id", session_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                reqObj.put("session_id", getSessionID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {

            reqObj.put("breaq_code","BQBPN000101");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        addProductToCart("BQBPN000101", reqObj);
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onCameraPermissionDenied() {
        startActivity(new Intent(mContext, ProductScanActivity.class));
        finish();
    }

    @Override
    public void onScanError(String s) {
        startActivity(new Intent(mContext, ProductScanActivity.class));
        finish();
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }

    private void addProductToCart(String itemName, JSONObject reqObj){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.kAdd_Product_url, reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(Constants.isLogEnable) {

                            Log.d(QRScanActivity.class.getSimpleName(), response.toString());
                        }

                        try {

                            if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {

                                BreaQApplication.mInstance.showToast(response.getString(Constants.kStatusMessage));

                                ScanData newData = ScanData.fromJson();

                                Intent intent = new Intent(QRScanActivity.this, YourCart.class);
                                intent.putExtra("scannedData", "");
                                startActivity(intent);
                                if(Utilities.has(ProductScanActivity.mProductScanActivity))
                                ProductScanActivity.mProductScanActivity.finish();
                                finish();

                            } else {

                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

        BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kAdd_Product_url);
    }

    private String getSessionID(){
        String res = LocalStorage.getStringPreference(mContext,"sessionData", response.toString());
        try {
            response = new JSONObject(res);
            if(response.has("session_id")) {
                session_id = response.getString("session_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return session_id;
    }
}