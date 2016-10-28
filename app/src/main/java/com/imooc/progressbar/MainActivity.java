package com.imooc.progressbar;

import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.imooc.view.HorizontalProgressbarWithProgress;
import com.imooc.view.RoundProgressBarWithProgress;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgressbarWithProgress mHProgress;
    private RoundProgressBarWithProgress mRProgress;
    private final static int MSG_UPDATE = 0x110;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mHProgress.getProgress();
            ++progress;
            mHProgress.setProgress(progress);
            mRProgress.setProgress(progress);
            if (progress >= 100){
                mHandler.removeMessages(MSG_UPDATE);
            }else {
                mHandler.sendEmptyMessageDelayed(MSG_UPDATE,100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("suncj","onCreate");
        setContentView(R.layout.activity_main);

        mHProgress = (HorizontalProgressbarWithProgress)findViewById(R.id.id_progress01);
        mRProgress = (RoundProgressBarWithProgress) findViewById(R.id.id_progress02);
        mHandler.sendEmptyMessage(MSG_UPDATE);   //正式项目需要另起一个线程做这些耗时操作
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("suncj","onBackPressed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("suncj","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("suncj","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("suncj","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("suncj","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("suncj","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("suncj","onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("suncj","onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("suncj","onSaveInstanceState");
    }
}
