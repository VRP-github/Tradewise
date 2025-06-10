package com.trade.tradewise.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trade.tradewise.R
import com.trade.tradewise.databinding.ActivityIntroScreenBinding

class IntroScreen : AppCompatActivity() {
    lateinit var binding: ActivityIntroScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIntroScreenBinding.inflate(layoutInflater)
       // enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnSignin.setOnClickListener{

            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignup.setOnClickListener{

            var intent=Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}