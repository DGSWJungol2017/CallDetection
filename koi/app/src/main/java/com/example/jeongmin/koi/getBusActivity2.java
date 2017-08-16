package com.example.jeongmin.koi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class getBusActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bus2);
    }

    public void getBusBtn2Clicked(View view) {
        Intent intent = new Intent(this, getBusActivity3.class);
        startActivity(intent);
        finish();
    }
}
