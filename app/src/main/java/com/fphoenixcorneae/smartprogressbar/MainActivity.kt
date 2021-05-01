package com.fphoenixcorneae.smartprogressbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isFullReduction = false
    private var isShowTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
    }

    private fun initView() {
        pblProgress.addProgressAnimatorUpdateListener {

        }
        pblProgress.addProgressAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                Log.i("onAnimationEnd", "onAnimationEnd")
            }
        })
    }

    private fun initListener() {
        btnShowTemperatureText.setOnClickListener {
            isShowTime = false
            var beginTemperature = when (isFullReduction) {
                true -> 100f
                else -> 80f
            }
            val endTemperature = when (isFullReduction) {
                true -> 80f
                else -> 100f
            }
            when {
                isFullReduction -> {
                    pblProgress.mSmartProgressBar.setProgressWithNoAnimation(0f)
                    pblProgress.mSmartProgressBar.setIsAnimated(true)
                    it.postDelayed({
                        beginTemperature = 99f
                        pblProgress.setTemperatureText(beginTemperature, endTemperature)
                    }, 3000)
                }
                else -> {
                    pblProgress.mSmartProgressBar.setIsAnimated(false)
                }
            }
            pblProgress.setTemperatureText(beginTemperature, endTemperature)
        }
        btnShowTimeText.setOnClickListener {
            isShowTime = true
            pblProgress.setTimeText(60, isFullReduction)
        }
        btnTurnDirection.setOnClickListener {
            isFullReduction = !isFullReduction
            when (isShowTime) {
                true -> {
                    btnShowTimeText.performClick()
                }
                else -> {
                    btnShowTemperatureText.performClick()
                }
            }
        }
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