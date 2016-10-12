package com.example.nanchen.timerdemo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtnGetCode;
    private TimeCount mTimeCount;
    private Button mBtnGetCode2;
    private boolean timeFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnGetCode = (Button) findViewById(R.id.main_btn_get_code);
        mBtnGetCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeCount = null;
                mTimeCount = new TimeCount(60 * 1000, 1000);
                mTimeCount.start();
            }
        });

        mBtnGetCode2 = (Button) findViewById(R.id.main_btn_get_code_2);
        mBtnGetCode2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnGetCode2.setClickable(false);
                mBtnGetCode2.setBackgroundColor(getResources().getColor(R.color.btn_unable));
                timeFlag = true;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (int i = 59; i >= 0 && timeFlag; i--) {
                            try {
                                sleep(1000);
                                Message msg = Message.obtain();
                                msg.what = i;
                                mHandler.sendMessage(msg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what > 0) {
                mBtnGetCode2.setText("(" + msg.what + ")秒后重试");
            } else {
                mBtnGetCode2.setText("获取验证码");
                mBtnGetCode2.setClickable(true);
                mBtnGetCode2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }


    };


    /**
     * Activity 销毁的时候注意把所有引用置为空，防止内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimeCount = null;
        timeFlag = false;
    }

    /**
     * 实现倒计时的类
     */
    private class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 计时过程显示  按钮不可用 设置为灰色
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnGetCode.setClickable(false);
            mBtnGetCode.setBackgroundColor(getResources().getColor(R.color.btn_unable));
            mBtnGetCode.setText("(" + millisUntilFinished / 1000 + ")秒后重试");
        }

        /**
         * 计时结束调用
         */
        @Override
        public void onFinish() {
            mBtnGetCode.setClickable(true);
            mBtnGetCode.setText("获取验证码方式1");
            mBtnGetCode.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


}

