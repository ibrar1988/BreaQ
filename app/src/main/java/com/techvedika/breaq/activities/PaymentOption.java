package com.techvedika.breaq.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.techvedika.breaq.R;
import com.techvedika.breaq.extras.Utilities;

public class PaymentOption extends AppCompatActivity {

    private Context mContext;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        this.mContext = this;
        findViewById(R.id.ImgbackNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, YourCart.class));
                finish();
            }
        });

        findViewById(R.id.tvUpi_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pd = new ProgressDialog(mContext);
                    pd.setTitle("Payment");
                    pd.setMessage("Payment is in progress. Please wait ...");
                    pd.setCancelable(false);
                    pd.show();
                }catch (Exception ex){}

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, DeliveryActivity.class));
                        finish();
                    }
                },3000);
            }
        });

        findViewById(R.id.tvCard_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pd = new ProgressDialog(mContext);
                    pd.setTitle("Card Payment");
                    pd.setMessage("Payment is in progress. Please wait ...");
                    pd.setCancelable(false);
                    pd.show();
                } catch (Exception ex) {}

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, DeliveryActivity.class));
                        finish();
                    }
                },3000);
            }
        });

        findViewById(R.id.tvOffline_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pd = new ProgressDialog(mContext);
                    pd.setTitle("Offline Payment");
                    pd.setMessage("Please wait we're preparing for offline payment");
                    pd.setCancelable(false);
                    pd.show();
                } catch (Exception ex) {}

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, DeliveryActivity.class));
                        finish();
                    }
                },3000);
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, YourCart.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if(Utilities.has(pd) && pd.isShowing()){
            pd.dismiss();
        }
        super.onDestroy();
    }
}
