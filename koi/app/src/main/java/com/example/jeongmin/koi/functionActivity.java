package com.example.jeongmin.koi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class functionActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;

    ConstraintLayout translateBtn;
    ConstraintLayout alarmBtn;

    ImageView alarmIcon;
    TextView alarmText;

    Boolean isalarmOn = false;

    TelephonyManager telephonyManager;
    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (isalarmOn) {
//                Log.i("알림상태", "켜짐");
                if (state == TelephonyManager.CALL_STATE_RINGING) {
//                    Log.i("상태: ", "울리는중");
                    turnOnFlashLight();

                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
//                    Log.i("상태: ", "통화중");
                    turnOffFlashLight();
                } else {
//                    Log.i("상태: ", "통화끊음");
                    turnOffFlashLight();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        translateBtn = (ConstraintLayout)findViewById(R.id.translateBtn);
        alarmBtn = (ConstraintLayout)findViewById(R.id.alarmBtn);

        alarmIcon = (ImageView)findViewById(R.id.alarmIcon);
        alarmText = (TextView)findViewById(R.id.alarmText);

        flash();

        telephonyManager=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, phoneStateListener.LISTEN_CALL_STATE);
    }

    public void flash(){

        Boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(functionActivity.this).create();
            alert.setTitle("에러");
            alert.setMessage("이 기기가 플래시를 지원하지 않습니다.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 앱 강제종료
                            finish();
                            System.exit(0);
                        }
                    });
            alert.show();
            return;
        }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void translateBtnClicked(View v){
        Intent intent = new Intent(this, translateActivity.class);
        startActivity(intent);
    }

    public void alarmBtnClicked(View v){
        if(isalarmOn){
            alarmText.setText("전화 알림 꺼짐");
            alarmIcon.setImageResource(R.drawable.calloff);
            isalarmOn=false;
//            Log.i("수신알림", "알림꺼짐");

        }else{
            alarmText.setText("전화 알림 켜짐");
            alarmIcon.setImageResource(R.drawable.callon);
            isalarmOn=true;
//            Log.i("수신알림", "알림켜짐");
        }
    }


}
