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

public class ProductScanActivity extends AppCompatActivity {

    private Context mContext;
    private LinearLayout scannerLayout;
    public static ProductScanActivity mProductScanActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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
        startActivity(intent);
        finish();
    }
}
