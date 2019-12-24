package com.wkz.smartprogressbar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SmartProgressBar mSpbHorizontal;
    private SmartProgressBar mSpbVertical;
    private SmartProgressBar mSpbRing1;
    private SmartProgressBar mSpbRing2;
    private SmartProgressBar mSpbSector1;
    private SmartProgressBar mSpbSector2;
    private ProgressBarLayout mPblProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mSpbHorizontal = (SmartProgressBar) findViewById(R.id.spb_horizontal);
        mSpbVertical = (SmartProgressBar) findViewById(R.id.spb_vertical);
        mSpbRing1 = (SmartProgressBar) findViewById(R.id.spb_ring_1);
        mSpbRing2 = (SmartProgressBar) findViewById(R.id.spb_ring_2);
        mSpbSector1 = (SmartProgressBar) findViewById(R.id.spb_sector_1);
        mSpbSector2 = (SmartProgressBar) findViewById(R.id.spb_sector_2);
        mPblProgress = (ProgressBarLayout) findViewById(R.id.pbl_progress);
    }

    private void initData() {
    }
}
