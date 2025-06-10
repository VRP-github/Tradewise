package com.trade.tradewise

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.star.starmedicahub.baseactivity.BaseActivity
import com.star.starmedicahub.retrofit.TokenProvider
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.activity.IntroScreen


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startSplace()
    }
    fun startSplace()
    {
        Handler(Looper.getMainLooper()).postDelayed({

            if(!TokenProvider.getToken().isNullOrEmpty())
            {
                Log.d("tokej-",TokenProvider.getToken())
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            else{
                Log.d("tokej-","null")
                startActivity(Intent(this, IntroScreen::class.java))
                finish()
            }

        }, 3000)
    }
}