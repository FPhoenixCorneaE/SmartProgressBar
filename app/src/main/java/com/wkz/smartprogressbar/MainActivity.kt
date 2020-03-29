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
        initListener()
    }

    private fun initView() {
        pblProgress.mSmartProgressBar.setIsAnimated(false)
        pblProgress.mBeginTemperature = 90f
        pblProgress.max = 10f
        pblProgress.setTemperatureText(false)
        pblProgress.setProgressAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                Log.i("onAnimationEnd", "onAnimationEnd")
            }
        })
    }

    private fun initListener() {
        btnPause.setOnClickListener {
            pblProgress.pauseProgressAnimation()
        }
        btnResume.setOnClickListener {
            pblProgress.resumeProgressAnimation()
        }
        btnCancel.setOnClickListener {
            pblProgress.cancelProgressAnimation()
        }
    }
}