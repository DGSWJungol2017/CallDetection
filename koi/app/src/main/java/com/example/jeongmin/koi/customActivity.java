package com.example.jeongmin.koi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class customActivity extends AppCompatActivity {

    TextView customModeText;
    ImageView customModeIcon;
    int customModeFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        customModeText = (TextView)findViewById(R.id.customModeText);
        customModeIcon = (ImageView)findViewById(R.id.customModeIcon);
    }


    public void customModeClicked(View v){
        if(customModeFlag==0){
            customModeIcon.setImageResource(R.drawable.settingon);
            customModeText.setText("사용자 지정\n모드 켜짐");
            customModeFlag=1;
        }else{
            customModeIcon.setImageResource(R.drawable.settingoff);
            customModeText.setText("사용자 지정\n모드 꺼짐");
            customModeFlag=0;
        }
    }

    public void soundListClicked(View v){
        Intent intent = new Intent(this, soundListActivity.class);
        startActivity(intent);
    }

    public void addSoundBtnClicked(View v){
        Intent intent = new Intent(this, addSoundActivity.class);
        startActivity(intent);
    }
}
