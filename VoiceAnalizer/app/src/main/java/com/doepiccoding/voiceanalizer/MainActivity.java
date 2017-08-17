package com.doepiccoding.voiceanalizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends Activity {

	private static final int sampleRate = 8000;
	private AudioRecord audio;
	private int bufferSize;
	private double lastLevel = 0;
	private Thread thread;
	private static final int SAMPLE_DELAY = 75;
	private ImageView mouthImage;

	private CameraManager mCameraManager;
	private String mCameraId;
	private ImageButton mTorchOnOffButton;
	private Boolean isTorchOn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		mCameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
		try {
			mCameraId = mCameraManager.getCameraIdList()[0];
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}

		mouthImage = (ImageView)findViewById(R.id.mounthHolder);
		mouthImage.setKeepScreenOn(true);

		try {
			bufferSize = AudioRecord
					.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
		} catch (Exception e) {
			android.util.Log.e("TrackingFlow", "Exception", e);
		}
	}

	protected void onResume() {
		super.onResume();
		audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);

		audio.startRecording(); //녹음 시작
		thread = new Thread(new Runnable() { // 스레드 생성
			public void run() {
				while(thread != null && !thread.isInterrupted()){
					//Let's make the thread sleep for a the approximate sampling time
					try{Thread.sleep(SAMPLE_DELAY);}catch(InterruptedException ie){ie.printStackTrace();}
					readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(lastLevel > 0 && lastLevel <= 50){
								mouthImage.setImageResource(R.drawable.mouth4);
							}else if(lastLevel > 50 && lastLevel <= 100){
								mouthImage.setImageResource(R.drawable.mouth3);
							}else if(lastLevel > 100 && lastLevel <= 170){
								mouthImage.setImageResource(R.drawable.mouth2);
							}
							if(lastLevel > 170){
								mouthImage.setImageResource(R.drawable.mouth1);

								Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vibe.vibrate( 5000 ); // 5초 동안 진동
							}
						}
					});
				}
			}
		});
		thread.start();
	}


	
	/**
	 * Functionality that gets the sound level out of the sample
	 */
	private void readAudioBuffer() {

		try {
			short[] buffer = new short[bufferSize];

			int bufferReadResult = 1;

			if (audio != null) {

				// Sense the voice...
				bufferReadResult = audio.read(buffer, 0, bufferSize);
				double sumLevel = 0;
				for (int i = 0; i < bufferReadResult; i++) {
					sumLevel += buffer[i];
				}
				lastLevel = Math.abs((sumLevel / bufferReadResult));

				TextView tv = (TextView)findViewById(R.id.noise);
				tv.setText(""+lastLevel);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		thread.interrupt();
		thread = null;
		try {
			if (audio != null) {
				audio.stop();
				audio.release();
				audio = null;
			}
		} catch (Exception e) {e.printStackTrace();}
	}
}
