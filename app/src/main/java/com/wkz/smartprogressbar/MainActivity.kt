package com.wkz.smartprogressbar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mSpbHorizontal: SmartProgressBar? = null
    private var mSpbVertical: SmartProgressBar? = null
    private var mSpbRing1: SmartProgressBar? = null
    private var mSpbRing2: SmartProgressBar? = null
    private var mSpbSector1: SmartProgressBar? = null
    private var mSpbSector2: SmartProgressBar? = null
    private var mPblProgress: ProgressBarLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {
        mSpbHorizontal = findViewById<View>(R.id.spb_horizontal) as SmartProgressBar
        mSpbVertical = findViewById<View>(R.id.spb_vertical) as SmartProgressBar
        mSpbRing1 = findViewById<View>(R.id.spb_ring_1) as SmartProgressBar
        mSpbRing2 = findViewById<View>(R.id.spb_ring_2) as SmartProgressBar
        mSpbSector1 = findViewById<View>(R.id.spb_sector_1) as SmartProgressBar
        mSpbSector2 = findViewById<View>(R.id.spb_sector_2) as SmartProgressBar
        mPblProgress = findViewById<View>(R.id.pbl_progress) as ProgressBarLayout
    }

    private fun initData() {
        mPblProgress?.mSmartProgressBar?.setIsAnimated(false)
        mPblProgress?.max = 60f
        mPblProgress?.setTimeText(true)
    }
}