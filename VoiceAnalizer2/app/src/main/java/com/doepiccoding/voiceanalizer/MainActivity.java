package com.doepiccoding.voiceanalizer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Vibrator;





public class MainActivity extends Activity {

	private static final int sampleRate = 8000;
	private AudioRecord audio;
	private int bufferSize;
	private double lastLevel = 0;
	private Thread thread;
	private static final int SAMPLE_DELAY = 75;
	private ImageView mouthImage;

    private int different = 100;

    private SeekBar sb;
    private  TextView diffTv;

	final int MAX_CNT = 20;
	double[] volArr = new double[MAX_CNT];

	double volSum=0;
	double volAvg;
	static final int viveSec = 4000;

	private int i, j = 0
			;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        diffTv = (TextView) findViewById(R.id.diffMaxResult);

        sb = (SeekBar) findViewById(R.id.diffMax);
        sb.setProgress(different);
		diffTv.setText(String.valueOf(different));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
												different = progress;
											  	diffTv.setText(String.valueOf(different));

                                          }

                                          @Override
                                          public void onStartTrackingTouch(SeekBar seekBar) {

                                          }

                                          @Override
                                          public void onStopTrackingTouch(SeekBar seekBar) {


                                          }
                                      });
		
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



		if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
			//Manifest.permission.READ_CALENDAR이 접근 승낙 상태 일때
		} else{
			//Manifest.permission.READ_CALENDAR이 접근 거절 상태 일때
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
				//사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
			} else{
				//사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
			}

			//사용자에게 접근권한 설정을 요구하는 다이얼로그를 띄운다.
			//만약 사용자가 다시 보지 않기에 체크를 했을 경우엔 권한 설정 다이얼로그가 뜨지 않고,
			//곧바로 OnRequestPermissionResult가 실행된다.
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},0);

		}

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

//					final double[] volArr;
//					volArr = new double[20];



					runOnUiThread(new Runnable() {


						@Override
						public void run() {



							if(volAvg + different < lastLevel) {
								mouthImage.setImageResource(R.drawable.mouth1);

								Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
								vibe.vibrate(viveSec);//4초
							} else {
								mouthImage.setImageResource(R.drawable.mouth4);
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

				if(j >= MAX_CNT){
					volAvg = volSum / MAX_CNT;
					volSum = 0;

					volArr = null;
					volArr = new double[MAX_CNT];

					j = 0;
				}

				volSum += lastLevel;
				volArr[j] = lastLevel;

				j++;

				TextView tv = (TextView)findViewById(R.id.noise);
				tv.setText(""+volAvg);
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
