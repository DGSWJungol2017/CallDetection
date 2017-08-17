package com.example.jeongmin.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


// 플래시 및 카메라 - https://www.codeproject.com/Articles/1112813/Android-Flash-Light-Application-Tutorial-Using-Cam

// 전화상태 - http://blog.naver.com/PostView.nhn?blogId=tempests05&logNo=20142503735

public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mTorchOnOffButton;
    private Boolean isTorchOn=true;
    private Boolean isRinging=false;



    TelephonyManager telephonyManager;
    PhoneStateListener phoneStateListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if(state==TelephonyManager.CALL_STATE_RINGING){
                Log.i("상태: ", "울리는중");
                turnOnFlashLight();
            }else if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                turnOffFlashLight();
                Log.i("상태: ", "통화중");
//                flashLoop();
            }else{
                flashLoop(false);
                Log.i("상태: ", "통화끊음");
                turnOffFlashLight();
            }

//            switch (state){
//                case TelephonyManager.CALL_STATE_IDLE:
//                    Log.i("상태", "울림x 통화x");
//                    turnOffFlashLight();
//                    isRinging=false;
//                    flash();
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING:
//                    Log.i("상태", "울림o 통화x");
//                    isRinging=true;
//                    flashLoop();
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    Log.i("상태", "통화중");
//                    turnOffFlashLight();
//                    isRinging=false;
//                    flashLoop();
//                    break;
//
//                default: break;
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FlashLightActivity", "onCreate()");
        setContentView(R.layout.activity_main);
        mTorchOnOffButton = (ImageButton) findViewById(R.id.button_on_off);

        flash();

        telephonyManager=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, phoneStateListener.LISTEN_CALL_STATE);
    }

    public void flash(){
        isTorchOn = false;

        Boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
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

    public void flashLoop(Boolean isRinging) {
        while (isRinging) {
            SystemClock.sleep(100);
            turnOnFlashLight();
            SystemClock.sleep(100);
            turnOffFlashLight();
        }
    }

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
                mTorchOnOffButton.setImageResource(R.mipmap.ic_launcher);
                isTorchOn=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                mTorchOnOffButton.setImageResource(R.mipmap.ic_launcher);
                isTorchOn=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick(View view) {
        if(isTorchOn){
            turnOffFlashLight();
            isTorchOn=false;
        }else{
            turnOnFlashLight();
            isTorchOn=true;
        }
    }
}