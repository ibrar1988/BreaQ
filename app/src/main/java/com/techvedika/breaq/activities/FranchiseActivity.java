package com.techvedika.breaq.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FranchiseActivity extends AppCompatActivity implements View.OnClickListener, DialogClickInterface{

    private Context mContext;
    private TextView tvTitle;
    private TextView tvStartShopping;
    private TextView tvFranchiseType, tvFranchiseName;
    private RelativeLayout chooseFranchiseType, chooseFranchiseName;
    private JSONArray franchiseTypeArray,franchiseNameArray;
    private String[] franchiseTypeList,franchiseNameList;
    private String franchiseTypeId,franchiseNameId;
    private String franchiseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise);
        this.mContext = this;
        init();
    }

    private void init(){
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        String first = "Brea";
        String next = "<font color='#ff61b6'>Q</font>";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            tvTitle.setText(Html.fromHtml(first+next));
        } else {
            tvTitle.setText(Html.fromHtml(first+next,Html.FROM_HTML_MODE_LEGACY));
        }

        fetchFranchiseType();

        tvFranchiseType = (TextView)findViewById(R.id.tvFranchiseType);

        tvFranchiseName= (TextView)findViewById(R.id.tvFranchiseName);

        chooseFranchiseType = (RelativeLayout)findViewById(R.id.chooseFranchiseType);
        chooseFranchiseType.setOnClickListener(this);

        chooseFranchiseName = (RelativeLayout)findViewById(R.id.chooseFranchiseName);
        chooseFranchiseName.setOnClickListener(this);

        tvStartShopping = (TextView)findViewById(R.id.tvStartShopping);
        tvStartShopping.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseFranchiseType:
                if(Utilities.has(franchiseTypeList) && franchiseTypeList.length>0) {
                    if(Utilities.has(franchiseTypePopMenu)) {
                        franchiseTypePopMenu.show();
                    }
                } else {
                    fetchFranchiseType();
                    CustomAlertDialog.getInstance().showCustomDialog(mContext,"Franchise Type", "Sorry there is no franchise type","OK", null,0);
                }
                break;
            case R.id.chooseFranchiseName:
                if(Utilities.has(franchiseTypeId)) {
                    if (Utilities.has(franchiseNameList) && franchiseNameList.length > 0) {

                        if(Utilities.has(franchiseNamePopMenu)) {
                            franchiseNamePopMenu.show();
                        }

                    } else if (franchiseNameList.length == 0) {

                        CustomAlertDialog.getInstance().showCustomDialog(mContext, "Franchise Name", "Sorry there is no franchise available for this franchise type", "OK", null, 0);

                    } else {
                        CustomAlertDialog.getInstance().showCustomDialog(mContext, "Franchise Name", "Please select a franchise first", "OK", null, 0);
                    }
                } else {
                    CustomAlertDialog.getInstance().showCustomDialog(mContext, "Franchise Type", "Please select a franchise type first", "OK", null, 0);
                }
                break;
            case R.id.tvStartShopping:
                if(Utilities.has(franchiseName)) {

                    String user_id = LocalStorage.getStringPreference(mContext, "user_id", "");

                    if (Utilities.has(user_id) && !user_id.equals("")) {

                        JSONObject reqObj = new JSONObject();

                        try {

                            reqObj.put("user_id", user_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        createSession(reqObj);

                    } else {
                        CustomAlertDialog.getInstance().showCustomDialog(mContext, "User ID", "User id is missing. Kill the app and login again", "OK", null, 0);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void fetchFranchiseType () {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET , Constants.kGet_franchise_type_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(Constants.isLogEnable) {

                            Log.d(FranchiseActivity.class.getSimpleName(), response.toString());
                        }

                        try {

                            if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {
                                franchiseTypeArray = response.getJSONArray("data");
                                if(Utilities.has(franchiseTypeArray) && franchiseTypeArray.length() > 0) {
                                    showFranchiseTypePopMenu(franchiseTypeArray);
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

        BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kGet_franchise_type_url);

    }

    private void fetchAllFranchise (JSONObject reqObj) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.kGet_Franchise_url, reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(Constants.isLogEnable) {

                            Log.d(FranchiseActivity.class.getSimpleName(), response.toString());
                        }

                        try {

                            if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {
                                franchiseNameArray = response.getJSONArray("data");
                                if(Utilities.has(franchiseNameArray) && franchiseNameArray.length() > 0) {
                                    showFranchiseNamePopMenu(franchiseNameArray);
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

        BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kGet_Franchise_url);

    }

    private void createSession(JSONObject reqObj) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.kSession_Creation_url, reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(Constants.isLogEnable) {

                            Log.d(FranchiseActivity.class.getSimpleName(), response.toString());
                        }

                        try {

                            if (response.has(Constants.kStatus_Code) && response.getString(Constants.kStatus_Code).equals(Constants.kSuccessCode)) {

                                /*{
                                    "session_bag_id": "e1dcad35",
                                        "payment_status": "pending",
                                        "session_id": 2,
                                        "session_status": "active",
                                        "statusMessage": "Session bag created",
                                        "statusCode": "200"
                                }*/

                                LocalStorage.saveStringPreference(mContext,"sessionData", response.toString());

                                startActivity(new Intent(mContext, ProductScanActivity.class));

                                //finish();

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

        BreaQApplication.mInstance.addToRequestQueue(jsonObjectRequest, Constants.kSession_Creation_url);
    }

    private PopupMenu franchiseTypePopMenu;

    private void showFranchiseTypePopMenu(JSONArray arrList) {
        /*{
            "franchise_type_id": 1,
                "franchise_type_name": "Clothing",
                "status": "Active"
        }*/

        franchiseTypePopMenu = new PopupMenu(mContext, chooseFranchiseType);

        franchiseTypeList = new String[arrList.length()];

        for (int i=0; i<arrList.length(); i++) {

            try {

                JSONObject singleObj = arrList.getJSONObject(i);

                franchiseTypeList[i] = singleObj.getString("franchise_type_name");

                franchiseTypePopMenu.getMenu().add(singleObj.getString("franchise_type_name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        franchiseTypePopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String franchiseType = item.getTitle().toString();

                tvFranchiseType.setText(franchiseType);

                franchiseTypeId = getFranchiseID(franchiseType);

                JSONObject reqObj = new JSONObject();

                try {
                    reqObj.put("franchise_type_id", franchiseTypeId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                fetchAllFranchise(reqObj);

                return false;
            }
        });

        franchiseTypePopMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
    }

    private PopupMenu franchiseNamePopMenu;

    private void showFranchiseNamePopMenu(JSONArray arrList) {

        /*{
            "franchise_name": "BrandFactory",
                "franchise_id": 1,
                "no_of_branches": 1,
                "status": "Active"
        }*/

        franchiseNamePopMenu = new PopupMenu(mContext, chooseFranchiseName);

        franchiseNameList = new String[arrList.length()];

        for (int i=0; i<arrList.length(); i++) {

            try {

                JSONObject singleObj = arrList.getJSONObject(i);

                franchiseNameList[i] = singleObj.getString("franchise_name");

                franchiseNamePopMenu.getMenu().add(singleObj.getString("franchise_name"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        franchiseNamePopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                franchiseName = item.getTitle().toString();

                tvFranchiseName.setText(franchiseName);

                return false;
            }
        });

        franchiseNamePopMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
    }

    private String getFranchiseID(String franchiseTypeName){

        String id = null;

        if(Utilities.has(franchiseTypeArray) && franchiseTypeArray.length()>0) {

            for(int i=0; i<franchiseTypeArray.length();i++) {

                try {

                    JSONObject obj = franchiseTypeArray.getJSONObject(i);

                    if(obj.has("franchise_type_name")) {

                        //String st = obj.getString("franchise_type_name");

                        if(obj.getString("franchise_type_name").toLowerCase().equals(franchiseTypeName.toLowerCase())){

                            id = obj.getString("franchise_type_id");

                            //franchise_type_id

                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return id;
    }

    private String getFranchiseStatus(String franchiseTypeName){

        String status = null;

        if(Utilities.has(franchiseTypeArray) && franchiseTypeArray.length()>0) {

            for(int i=0; i<franchiseTypeArray.length();i++) {

                try {

                    JSONObject obj = franchiseTypeArray.getJSONObject(i);

                    if(obj.has("franchise_type_name")) {

                        if(obj.getString("franchise_type_name").toLowerCase().equals(franchiseTypeName.toLowerCase())){

                            status = obj.getString("status");

                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return status;
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
