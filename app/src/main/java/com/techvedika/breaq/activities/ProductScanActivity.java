package com.techvedika.breaq.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techvedika.breaq.R;
import com.techvedika.breaq.extras.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductScanActivity extends AppCompatActivity {

    private Context mContext;
    private TextView tvSession_id;
    private LinearLayout scannerLayout;
    public static ProductScanActivity mProductScanActivity;
    JSONObject response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_scan);
        mProductScanActivity = this;
        this.mContext = this;
        init();
    }

    private void init(){
        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        String first = "Brea";
        String next = "<font color='#ff61b6'>Q</font>";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            tvTitle.setText(Html.fromHtml(first+next));
        } else {
            tvTitle.setText(Html.fromHtml(first+next,Html.FROM_HTML_MODE_LEGACY));
        }

        tvSession_id = (TextView)findViewById(R.id.tvSession_id);

        String res = LocalStorage.getStringPreference(mContext,"sessionData", response.toString());

        try {

            response = new JSONObject(res);

            if(response.has("session_bag_id")) {

                tvSession_id.setText("Your Shopping Bag ID : "+response.getString("session_bag_id").toUpperCase());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        scannerLayout = (LinearLayout)findViewById(R.id.scannerLayout);

        scannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanItem();
            }
        });
    }
    private void scanItem(){
        Intent intent = new Intent(mContext, QRScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("isFrom",ProductScanActivity.class.getSimpleName());
        startActivity(intent);
        finish();
    }
}
