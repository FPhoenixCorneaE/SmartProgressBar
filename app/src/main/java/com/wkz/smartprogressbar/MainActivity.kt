package com.wkz.smartprogressbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {

    }

    private fun initData() {
        val beginTemperature = 60f
        val endTemperature = 100f
//        pbl_progress.setTemperatureText(beginTemperature, endTemperature)
        pbl_progress.setTimeText(60, false)
        pbl_progress.setProgressAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                Log.i("onAnimationEnd", "onAnimationEnd")
            }
        })
    }
}