package com.example.jeongmin.koi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout detectBtn;
    RelativeLayout busModeBtn;
    RelativeLayout functionBtn;
    RelativeLayout customBtn;

    ImageView detectIcon;
    TextView detectText;
    int detectFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detectBtn = (RelativeLayout)findViewById(R.id.detectBtn);
        busModeBtn = (RelativeLayout)findViewById(R.id.busModeBtn);
        functionBtn = (RelativeLayout)findViewById(R.id.functionBtn);
        customBtn = (RelativeLayout)findViewById(R.id.customBtn);

        detectIcon = (ImageView)findViewById(R.id.detectIcon);
        detectText = (TextView)findViewById(R.id.detectText);
    }

    //감지 모드 클릭시 이벤트
    public void detectBtnClicked (View v){

        if(detectFlag==0) {
            detectIcon.setImageResource(R.drawable.warnicon);
            detectText.setText("감지모드 켜짐");
            detectFlag=1;
        }else{
            detectIcon.setImageResource(R.drawable.warnoffpng);
            detectText.setText("감지모드 꺼짐");
            detectFlag=0;
        }
    }


    public void busModeBtnClicked (View v){
        Intent intent = new Intent(this, busModeActivity.class);
        startActivity(intent);
    }

    public void functionBtnClicked (View v){
        Intent intent = new Intent(this, functionActivity.class);
        startActivity(intent);
    }

    public void customBtnClicke (View v){
        Intent intent = new Intent(this, customActivity.class);
        startActivity(intent);
    }


}
