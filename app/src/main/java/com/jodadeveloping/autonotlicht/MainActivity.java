package com.jodadeveloping.autonotlicht;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private int delay;
    private ImageView imageView;
    private NewThread r;
    private Handler handler;
    private AdView mAdView;

    public class NewThread extends Thread {
        private int millis;

        @Override
        public void run() {
            while (true) {
                handler.sendEmptyMessage(0);
                delay(millis);
                handler.sendEmptyMessage(1);
                delay(millis);
            }
        }

        //Задержка в мс
        private void delay(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void setMillis(int millis) {
            this.millis = millis;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

                MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        imageView = findViewById(R.id.imageView);
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    imageView.setVisibility(View.INVISIBLE);
                }
                if (msg.what == 1) imageView.setVisibility(View.VISIBLE);
            }
        };
        r = new NewThread();
        r.setMillis(seekBar.getProgress());
        r.start();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        r.setMillis(seekBar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        r.setMillis(seekBar.getProgress());
    }


}