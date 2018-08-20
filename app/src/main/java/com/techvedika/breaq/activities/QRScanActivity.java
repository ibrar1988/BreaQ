package com.techvedika.breaq.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.techvedika.breaq.R;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class QRScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private Context mContex;

    BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        this.mContex = this;

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContex, ProductScanActivity.class));
        finish();
    }

    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();
        // ticket details activity by passing barcode
        Intent intent = new Intent(QRScanActivity.this, DashBoardActivity.class);
        intent.putExtra("scannedData", barcode.displayValue);
        startActivity(intent);
        ProductScanActivity.mProductScanActivity.finish();
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onCameraPermissionDenied() {
        startActivity(new Intent(mContex, ProductScanActivity.class));
        finish();
    }

    @Override
    public void onScanError(String s) {
        startActivity(new Intent(mContex, ProductScanActivity.class));
        finish();
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }
}