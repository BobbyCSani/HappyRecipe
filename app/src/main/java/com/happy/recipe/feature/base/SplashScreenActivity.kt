package com.happy.recipe.feature.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happy.recipe.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySplashScreenBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
        }
    }
}