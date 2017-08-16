package com.example.jeongmin.koi;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class functionActivity extends AppCompatActivity {

    ConstraintLayout translateBtn;
    ConstraintLayout alarmBtn;

    ImageView alarmIcon;
    TextView alarmText;

    int alarmFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        translateBtn = (ConstraintLayout)findViewById(R.id.translateBtn);
        alarmBtn = (ConstraintLayout)findViewById(R.id.alarmBtn);

        alarmIcon = (ImageView)findViewById(R.id.alarmIcon);
        alarmText = (TextView)findViewById(R.id.alarmText);
    }

    public void translateBtnClicked(View v){
        Intent intent = new Intent(this, translateActivity.class);
        startActivity(intent);
    }

    public void alarmBtnClicked(View v){

        if(alarmFlag == 0){
            alarmText.setText("전화 알림 켜짐");
            alarmIcon.setImageResource(R.drawable.callon);
            alarmFlag=1;
        }else{
            alarmText.setText("전화 알림 꺼짐");
            alarmIcon.setImageResource(R.drawable.calloff);
            alarmFlag=0;
        }
    }
}
