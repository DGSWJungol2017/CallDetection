package com.example.jeongmin.koi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class addSoundActivity extends AppCompatActivity {

    ImageView volumeIcon;
    int volumeIconFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sound);

        volumeIcon = (ImageView)findViewById(R.id.volumeIcon);
    }

    public void saveBtnClicked(View v){
        finish();
    }

    public void volumeBtnClicked(View view) {
        if(volumeIconFlag==0){
            volumeIcon.setImageResource(R.drawable.volumeoff);
            volumeIconFlag=1;
        }else{
            volumeIcon.setImageResource(R.drawable.volume);
            volumeIconFlag=0;
        }
    }
}
