package com.example.yessh.pomodoro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;


public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private TextView countdownText;
    private TextView task;
    private Button countdownButton;
    private Button countdownShort;


    private CountDownTimer countDownTimer;
    private long timeLeftInMiliSeconds = 1500000; //25min;

    private boolean timerRunning;
    private int countPomodoro = 0;

    private RingProgressBar ringProgressBar;
    private int pomProgressStatus = 0;
    private Handler pomHandler ;

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
    /* public void starStop(){
        if(timerRunning){
            stopTimer();
        }else {
            startTimer();
        }
    }*/
    public void startTimer(){
        timeLeftInMiliSeconds = 1500000; //25min

        countDownTimer = new CountDownTimer(timeLeftInMiliSeconds,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMiliSeconds = l;
                ringProgressBar = (RingProgressBar) findViewById(R.id.ringProgressBar);
                updateTimer();
                countdownButton.setEnabled(false);
                pomHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        if (msg.what == 0){
                            if (pomProgressStatus<1500){
                                pomProgressStatus++;
                                ringProgressBar.setProgress(pomProgressStatus);
                            }
                        }
                    }
                };
                ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
                    @Override
                    public void progressToComplete() {
                        pomProgressStatus = 0;
                    }
                });

                new  Thread(new Runnable() {
                    @Override
                    public void run() {

                            try {
                                Thread.sleep(1000);
                                pomHandler.sendEmptyMessage(0);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }

                        }
                }).start();
            }

            @Override
            public void onFinish() {

                countdownText.setText("0:00");
                countdownButton.setEnabled(true);
                countdownButton.setText("Start Short Break");
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
                        task.setText("Break");
                        countdownShort.setText("Stop");
                        if (countPomodoro<=4) {
                            breakTimer(300000);
                        }else{
                            breakTimer(600000);
                        }

                    }
                });
            }
        }.start();
        countdownButton.setText("Pause");
        timerRunning = true;
    }
    public void stopTimer(){
        countDownTimer.cancel();
        countdownButton.setText("Stop");
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
    public void breakTimer(long seconds){
        timeLeftInMiliSeconds = seconds;
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
                countdownButton.setText("Start Pomodoro");
                mp = MediaPlayer.create(MainActivity.this,R.raw.guitar);
                mp.start();
                countdownShort =(Button)findViewById(R.id.countdown_button);
                countdownShort.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        // Intent countdownShort = new Intent(MainActivity.this, ShortBreak.class);
                        //startActivity(countdownShort);
                        mp.stop();
                        TextView task = (TextView) findViewById(R.id.task);
                        task.setText("FulFill your Task");
                        countdownShort.setText("Stop");
                        pomProgressStatus = 0;
                        startTimer();
                    }
                });
            }
        }.start();
        countdownButton.setText("Pause");
        countPomodoro ++;
        timerRunning = true;
    }

}
