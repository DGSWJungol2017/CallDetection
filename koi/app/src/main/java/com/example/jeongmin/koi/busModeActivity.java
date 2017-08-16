package com.example.jeongmin.koi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class busModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_mode);
    }

    public void startBtnClicked(View view) {
        Intent intent = new Intent(this, getBusActivity.class);
        startActivity(intent);
    }
}
