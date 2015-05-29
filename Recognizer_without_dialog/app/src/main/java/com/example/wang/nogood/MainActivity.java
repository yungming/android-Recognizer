package com.example.wang.nogood;

//插件

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements
        RecognitionListener {
    //宣告
    private TextView returnedText;
    private ToggleButton toggleButton;//切換按鈕
    private ProgressBar progressBar;//進度條
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //物件對映圖形XML ID
        returnedText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);

        /*
        android view setVisibility():
        有三個參數：Parameters: visibility One of VISIBLE , INVISIBLE , or GONE，想對應的三個常量值：0、4、8
        VISIBLE:0 意思是可見的
        INVISIBILITY:4 意思是不可見的，但還佔著原來的空間
        GONE:8 意思是不可見的，不佔用原來的佈局空間
         */
        progressBar.setVisibility(View.INVISIBLE);

        //初始化識別語音工具
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        //注冊回調類及函數
        speech.setRecognitionListener(this);

        //通過Intent傳遞語音辨識的模式
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //語音設定語言-英文
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        //語音在PackageName執行，this.getPackageName()是這個APP的PackageName->com.example.wang.nogood
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        //語言模式和自由形式的語音辨識
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        //切換按鈕執行程式
        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {//開啟
                    //進度條顯示
                    progressBar.setVisibility(View.VISIBLE);
                    //進度條開啟
                    progressBar.setIndeterminate(true);
                    //語音啟動
                    speech.startListening(recognizerIntent);
                } else {//關閉
                    //進度條關閉
                    progressBar.setIndeterminate(false);
                    //進度條不顯示
                    progressBar.setVisibility(View.INVISIBLE);
                    //語音關閉
                    speech.stopListening();
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + Arrays.toString(buffer));
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        //辨識講話的字
        for (String result : matches)
        text += result + "\n";

        returnedText.setText(text);

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "語音錯誤";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "客戶端錯誤";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "權限不足";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "網絡錯誤";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "連線逾時";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "找不到";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "語音忙碌";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "從服務器錯誤";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "沒有語音輸入";
                break;
            default:
                message = "不明白，請重試。";
                break;
        }
        return message;
    }







}