package com.techvedika.breaq.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.techvedika.breaq.R;

public class FranchiseActivity extends AppCompatActivity {

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise);
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
    }
}
