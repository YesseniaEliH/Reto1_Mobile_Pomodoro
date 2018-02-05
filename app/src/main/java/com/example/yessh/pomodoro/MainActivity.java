package com.example.yessh.pomodoro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private TextView countdownText;
    private TextView task;
    private Button countdownButton;
    private Button countdownShort;

    private CountDownTimer countDownTimer;
    private long timeLeftInMiliSeconds = 1500000; //25min

    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownText = findViewById(R.id.countdown_text);
        countdownButton = findViewById(R.id.countdown_button);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
        updateTimer();
    }
    public void starStop(){
        if(timerRunning){
            stopTimer();
        }else {
            startTimer();
        }
    }
    public void startTimer(){

        countDownTimer = new CountDownTimer(timeLeftInMiliSeconds,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMiliSeconds = l;
                updateTimer();
                countdownButton.setEnabled(false);
            }

            @Override
            public void onFinish() {

                countdownText.setText("0:00");
                countdownButton.setEnabled(true);
                countdownButton.setText("START SHORT BREAK");
                mp = MediaPlayer.create(MainActivity.this,R.raw.minuet_bach);
                mp.start();
                countdownShort =(Button)findViewById(R.id.countdown_button);
                countdownShort.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                       // Intent countdownShort = new Intent(MainActivity.this, ShortBreak.class);
                        //startActivity(countdownShort);
                        mp.stop();
                        TextView task = (TextView) findViewById(R.id.task);
                        task.setText("BREAK");
                        countdownShort.setText("STOP");
                        timeLeftInMiliSeconds = 50000;
                        startTimer();

                    }
                });
            }
        }.start();
        countdownButton.setText("PAUSE");
        timerRunning = true;
    }
    public void stopTimer(){
        countDownTimer.cancel();
        countdownButton.setText("STOP");
        timerRunning = false;
    }
    public void updateTimer(){
        int minutes = (int) timeLeftInMiliSeconds / 60000;
        int seconds = (int) timeLeftInMiliSeconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }


}
