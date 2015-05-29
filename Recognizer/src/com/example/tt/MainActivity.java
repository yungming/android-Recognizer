package com.example.tt;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
 private static final int RQS_VOICE_RECOGNITION = 1;
 TextView textResult;
 String setting = "設定";

	private ImageView mImg;
	   private DisplayMetrics mPhone;
	   private final static int CAMERA = 66 ;
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  Button buttonSpeech = (Button) findViewById(R.id.Speech);
  textResult = (TextView) findViewById(R.id.Result);
  buttonSpeech.setOnClickListener(new Button.OnClickListener() {
   @Override
   public void onClick(View arg0) {
    Intent intent = new Intent(
      RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speech");
    startActivityForResult(intent, RQS_VOICE_RECOGNITION);
   }
  });
 }

 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

  speechRecognitionAction(requestCode, resultCode, data);
 }

 public void speechRecognitionAction(int requestCode, int resultCode,
   Intent data) {

  if (requestCode == RQS_VOICE_RECOGNITION) {
   if (resultCode == RESULT_OK) {
    ArrayList<String> result = data
      .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    String tempStr = "";
    for (String str : result) {
     tempStr += str + "\n";
    }


    
    for (int i = 0; i < result.size(); i++) 
    {

        if (setting.equals(result.get(i))) {
            Intent intent = new Intent(
              "com.android.settings.TTS_SETTINGS");
            startActivity(intent);
        }
    }
    
    textResult.setText(tempStr);
   }
  }
 }
 



}
