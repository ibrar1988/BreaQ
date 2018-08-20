package com.techvedika.breaq.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

import com.techvedika.breaq.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        String first = "Brea";
        //#FF1493
        //#ff61b6

        String next = "<font color='#ff61b6'>Q</font>";
        TextView tv = (TextView)findViewById(R.id.splash_title);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(first+next));
        } else {
            tv.setText(Html.fromHtml(first+next,Html.FROM_HTML_MODE_LEGACY));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, FranchiseActivity.class));
                finish();
            }
        },2000);
    }
}
