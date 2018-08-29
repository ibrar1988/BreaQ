package com.techvedika.breaq.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.techvedika.breaq.R;
import com.techvedika.breaq.adapters.CartItemAdapter;
import com.techvedika.breaq.constant.Constants;
import com.techvedika.breaq.extras.Common;
import com.techvedika.breaq.extras.CustomAlertDialog;
import com.techvedika.breaq.extras.LocalStorage;
import com.techvedika.breaq.extras.Log;
import com.techvedika.breaq.extras.Utilities;
import com.techvedika.breaq.models.ScanData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class YourCart extends AppCompatActivity implements View.OnClickListener, BarcodeReader.BarcodeReaderListener{

    private static final int SCAN_SCREEN_RESULT = 200;
    private Context mContext;

    private CartItemAdapter mCartItemAdapter;
    private List<ScanData> mCartItemList;

    private LinearLayout emptyCartLayout;
    private RecyclerView cartRecyclerView;
    private TextView tvSession_id;
    private Button btnCheckout, btnContinueShopping;
    private TextView tvCurrentSubTotal;
    String isFrom;
    double totalAmount = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);
        this.mContext = this;
        mCartItemList = new ArrayList<>();
        init();
    }

    private void init(){
        Intent intentData = getIntent();
        isFrom = intentData.getStringExtra("isFrom");
        tvSession_id = (TextView)findViewById(R.id.tvSession_id);
        emptyCartLayout = (LinearLayout)findViewById(R.id.emptyCartLayout);
        cartRecyclerView = (RecyclerView)findViewById(R.id.cartRecyclerView);
        tvCurrentSubTotal =(TextView)findViewById(R.id.tvCurrentSubTotal);
        btnCheckout = (Button)findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);
        btnContinueShopping = (Button)findViewById(R.id.btnContinueShopping);
        btnContinueShopping.setOnClickListener(this);
        String res = LocalStorage.getStringPreference(mContext,"sessionData", "");

        try {

            JSONObject response = new JSONObject(res);

            if(response.has("session_bag_id")) {

                tvSession_id.setText("Your Shopping Bag ID : "+response.getString("session_bag_id").toUpperCase());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckout:
                Intent intent = new Intent(mContext, PaymentOption.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);finish();
                break;
            case R.id.btnContinueShopping:
                Intent _intent = new Intent(mContext, ProductScanActivity.class);
                _intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(_intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if(Utilities.has(isFrom)) {

            if(isFrom.equalsIgnoreCase(ProductScanActivity.class.getSimpleName())) {
                Intent intent = new Intent(mContext, ProductScanActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        }
    }

    private void initRecyclerView() {

        try {

            JSONObject obj = new JSONObject();
            obj.put("productPrice", 399);
            obj.put("productName", "BQBPN000101");
            obj.put("materialType", "Metallic");
            obj.put("materialColor", "Silver");
            obj.put("totalItemPrice", 399);
            obj.put("materialCount", 1);

            mCartItemList.add(0,new ScanData(obj.getDouble("productPrice"), obj.getString("productName"), obj.getString("materialType"), obj.getString("materialColor") , obj.getDouble("totalItemPrice"), "",obj.getInt("materialCount")));

            JSONObject obj1 = new JSONObject();
            obj1.put("productPrice", 270.34);
            obj1.put("productName", "BQBPN000102");
            obj1.put("materialType", "Metallic");
            obj1.put("materialColor", "Pink");
            obj1.put("totalItemPrice", 270.34);
            obj1.put("materialCount", 1);

            mCartItemList.add(1,new ScanData(obj1.getDouble("productPrice"), obj1.getString("productName"), obj1.getString("materialType"), obj1.getString("materialColor") , obj1.getDouble("totalItemPrice"), "",obj1.getInt("materialCount")));

            JSONObject obj2 = new JSONObject();
            obj2.put("productPrice", 550.47);
            obj2.put("productName", "BQBPN000103");
            obj2.put("materialType", "Fabric");
            obj2.put("materialColor", "Gold");
            obj2.put("totalItemPrice", 550.47);
            obj2.put("materialCount", 1);

            mCartItemList.add(2,new ScanData(obj2.getDouble("productPrice"), obj2.getString("productName"), obj2.getString("materialType"), obj2.getString("materialColor") , obj2.getDouble("totalItemPrice"), "",obj2.getInt("materialCount")));

            totalAmount = 399+270.34+550.47;

            // Cart subtotal (1 Item) : Rs 480.00

            tvCurrentSubTotal.setText("Cart subtotal ("+mCartItemList.size()+" Item) : Rs "+Common.formatCurrency(totalAmount));

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        mCartItemAdapter = new CartItemAdapter(mCartItemList, new CartItemAdapter.OnItemClickListener() {
            @Override
            public void onUpdateItem(ScanData item) {

                BreaQApplication.mInstance.showToast("Coming soon!!!", Toast.LENGTH_LONG);
            }

            @Override
            public void onRemoveItem(ScanData item) {
                AlertDialog confirmAlert = new AlertDialog.Builder(mContext)
                        .setTitle("Confirmation")
                        .setTitle("Are you sure want to remove the item from your cart")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the item from cart
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                confirmAlert.show();
            }

            @Override
            public void onIncreaseItem() {
                BreaQApplication.mInstance.showToast("Coming soon!!!", Toast.LENGTH_LONG);
            }

            @Override
            public void onDecreaseItem() {
                BreaQApplication.mInstance.showToast("Coming soon!!!", Toast.LENGTH_LONG);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        cartRecyclerView.setLayoutManager(mLayoutManager);

        cartRecyclerView.setItemAnimator(new DefaultItemAnimator());

        cartRecyclerView.setAdapter(mCartItemAdapter);

        mCartItemAdapter.notifyDataSetChanged();

        getCartItems();
    }

    private void getCartItems(){
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("session_id",getSessionID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.kGet_Cart_Product_url, reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(Constants.isLogEnable) {

                            Log.d(YourCart.class.getSimpleName(), response.toString());
                        }

                        try {

                            if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {

                                JSONArray arrayData = response.getJSONArray("data");

                                if(arrayData.length()>0) {

                                    JSONObject jsoData = arrayData.getJSONObject(0);

                                    JSONArray listOfItem = jsoData.getJSONArray("products");


                                }

                            } else {

                                CustomAlertDialog.getInstance().showCustomDialog(mContext, "BreaQ", response.getString(Constants.kStatusMessage), "OK",null,0);
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

        BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kGet_Cart_Product_url);
    }

    private void scanItem(){
        Intent intent = new Intent(mContext, QRScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, SCAN_SCREEN_RESULT);
    }

    @Override
    public void onScanned(Barcode barcode) {

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }
private String session_id;
    private String getSessionID(){
        String res = LocalStorage.getStringPreference(mContext,"sessionData", "");
        try {
            JSONObject response = new JSONObject(res);
            if(response.has("session_id")) {
                session_id = response.getString("session_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return session_id;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SCAN_SCREEN_RESULT) {

            if(resultCode == Activity.RESULT_OK) {

                String scannedData = data.getStringExtra("scannedData");

                BreaQApplication.mInstance.showToast("Data is scanned successfully");



            } else {

                BreaQApplication.mInstance.showToast("Scan is failed");
            }
        }
    }
}
