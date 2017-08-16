package com.example.jeongmin.koi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class translateActivity extends Activity implements OnClickListener {
    private final int GOOGLE_STT = 1000, MY_UI=1001;				//requestCode. ���������ν�, ���� ���� Activity
    private ArrayList<String> mResult;									//�����ν� ��� ������ list
    private String mSelectedString;										//��� list �� ����ڰ� ������ �ؽ�Ʈ
    private TextView ResultTextView;									//���� ��� ����ϴ� �ؽ�Ʈ ��

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        //findViewById(R.id.hide).setOnClickListener(this);				//���� ���� activity �̿�.

        ResultTextView = (TextView)findViewById(R.id.resultTxt);		//��� ��� ��
    }

    @Override
    public void onClick(View v) {
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);			//intent ����
            i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());	//�����ν��� ȣ���� ��Ű��
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");							//�����ν� ��� ����
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말을 하세요..");						//����ڿ��� ���� �� ����

            startActivityForResult(i, GOOGLE_STT);												//���� �����ν� ����
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if( resultCode == RESULT_OK  && (requestCode == GOOGLE_STT || requestCode == MY_UI) ){		//결과가 있으면
            showSelectDialog(requestCode, data);				//결과를 다이얼로그로 출력.
        }
        else{															//결과가 없으면 에러 메시지 출력
            String msg = null;

            //내가 만든 activity에서 넘어오는 오류 코드를 분류
            switch(resultCode){
                case SpeechRecognizer.ERROR_AUDIO:
                    msg = "오디오 입력 중 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    msg = "단말에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "권한이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    msg = "네트워크 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "네트워크 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    msg = "음성인식 서비스가 과부하 되었습니다.";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    msg = "서버에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "입력이 없습니다.";
                    break;
            }

            if(msg != null)		//���� �޽����� null�� �ƴϸ� �޽��� ���
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    //��� list ����ϴ� ���̾�α� ����
    private void showSelectDialog(int requestCode, Intent data){
        String key = "";
        if(requestCode == GOOGLE_STT)					//���������ν��̸�
            key = RecognizerIntent.EXTRA_RESULTS;	//Ű�� ����
        else if(requestCode == MY_UI)					//���� ���� activity �̸�
            key = SpeechRecognizer.RESULTS_RECOGNITION;	//Ű�� ����

        mResult = data.getStringArrayListExtra(key);		//�νĵ� ������ list �޾ƿ�.
        String[] result = new String[mResult.size()];			//�迭����. ���̾�α׿��� ����ϱ� ����
        mResult.toArray(result);									//	list �迭�� ��ȯ

        //1�� �����ϴ� ���̾�α� ����
        AlertDialog ad = new AlertDialog.Builder(this).setTitle("말씀하신 내용을 선택해주세요!")
                .setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        mSelectedString = mResult.get(which);		//�����ϸ� �ش� ���� ����
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
//									mResultTextView.setText("인식결과 : "+mSelectedString);		//Ȯ�� ��ư ������ ��� ���
                        Toast.makeText(translateActivity.this, mSelectedString, Toast.LENGTH_SHORT).show();

                        ResultTextView .append(mSelectedString + "\n");
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
//									mResultTextView.setText("");		//��ҹ�ư ������ �ʱ�ȭ
                        mSelectedString = null;
                        ResultTextView .append("");
                    }
                }).create();
        ad.show();
    }
}