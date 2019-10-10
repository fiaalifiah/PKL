package com.apps.pkl;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

public class RatingDashboard extends Activity {
    private int waktu_loading = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratingdashboard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(RatingDashboard.this,UserActivity.class);
                startActivity(home);
                finish();

            }
        }, waktu_loading);
    }
}
