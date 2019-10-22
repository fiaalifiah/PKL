package com.apps.pkl;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ActivityTime extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);


        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        btn_start = (Button)findViewById(R.id.btn_start);
        tv_time= (TextView)findViewById(R.id.tv_timer);
        et_timer = (EditText)findViewById(R.id.et_timer);



        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn_countdown();
            }
        });
    }

            int myProgress = 0;
            ProgressBar progressBarView;
            Button btn_start;
            TextView tv_time;
            EditText et_timer;
            int progress;
            CountDownTimer countDownTimer;
            int endTime = 250;




            private void fn_countdown() {

                if (et_timer.getText().toString().length()>0) {
                    myProgress = 0;

                    try {
                        countDownTimer.cancel();

                    } catch (Exception e) {

                    }

                    String timeInterval = et_timer.getText().toString();
                    progress = 1;





                    endTime = Integer.parseInt(timeInterval); // up to finish time

                    countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            setProgress(progress, endTime);
                            progress = progress + 1;
                            int seconds = (int) (millisUntilFinished / 1000) % 60;
                            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                            String newtime = hours + ":" + minutes + ":" + seconds;

                            if (newtime.equals("0:0:0")) {
                                tv_time.setText("00:00:00");
                            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                                tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
                            } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
                            } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
                            } else if (String.valueOf(hours).length() == 1) {
                                tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
                            } else if (String.valueOf(minutes).length() == 1) {
                                tv_time.setText(hours + ":0" + minutes + ":" + seconds);
                            } else if (String.valueOf(seconds).length() == 1) {
                                tv_time.setText(hours + ":" + minutes + ":0" + seconds);
                            } else {
                                tv_time.setText(hours + ":" + minutes + ":" + seconds);
                            }

                        }

                        @Override
                        public void onFinish() {
                            setProgress(progress, endTime);


                        }
                    };
                    countDownTimer.start();
                }else {
                    Toast.makeText(getApplicationContext(),"Please enter the value",Toast.LENGTH_LONG).show();
                }

            }

            public void setProgress(int startTime, int endTime) {
                progressBarView.setMax(endTime);
                progressBarView.setSecondaryProgress(endTime);
                progressBarView.setProgress(startTime);

            }

}


