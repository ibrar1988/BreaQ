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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.techvedika.breaq.R;
import com.techvedika.breaq.adapters.CartItemAdapter;
import com.techvedika.breaq.models.ScanData;

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
    private TextView session_id;
    private Button btnCheckout, btnContinueShopping;
    private TextView tvCurrentSubTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_cart);
        this.mContext = this;
        init();
    }

    private void init(){
        emptyCartLayout = (LinearLayout)findViewById(R.id.emptyCartLayout);
        cartRecyclerView = (RecyclerView)findViewById(R.id.cartRecyclerView);
        btnCheckout = (Button)findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);
        btnContinueShopping = (Button)findViewById(R.id.btnContinueShopping);
        btnContinueShopping.setOnClickListener(this);
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckout:
                break;
            case R.id.btnContinueShopping:
                break;
            default:
                break;
        }
    }

    private void initRecyclerView() {
        mCartItemList = new ArrayList<>();
        mCartItemAdapter = new CartItemAdapter(mCartItemList, new CartItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScanData item) {
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
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        cartRecyclerView.setLayoutManager(mLayoutManager);

        cartRecyclerView.setItemAnimator(new DefaultItemAnimator());

        cartRecyclerView.setAdapter(mCartItemAdapter);
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
