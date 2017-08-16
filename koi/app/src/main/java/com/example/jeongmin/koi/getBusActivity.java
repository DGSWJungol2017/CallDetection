package com.example.jeongmin.koi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class getBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bus);
    }

    public void getBusBtnClicked(View view) {
        Intent intent = new Intent(this, getBusActivity2.class);
        startActivity(intent);
        finish();
    }
}
