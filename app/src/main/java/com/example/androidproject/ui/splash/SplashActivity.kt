package com.example.androidproject.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        initBinding()
        handleLottieAnimation()
    }

    private fun initObservers() {
        viewModel.nextScreen.observe(this) { targetActivity ->
            targetActivity?.let {
                startActivity(Intent(this, it))
                finish()
            }
        }
    }

    private fun initBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun handleLottieAnimation() {
        binding.animationView.addAnimatorUpdateListener { valueAnimator ->
            val progress = (valueAnimator.animatedValue as Float * 100).toInt()
            if (progress >= 100) viewModel.decideNextScreen()
        }
    }
}
