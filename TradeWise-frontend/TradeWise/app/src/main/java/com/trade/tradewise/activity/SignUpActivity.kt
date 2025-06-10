package com.trade.tradewise.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.star.starmedicahub.baseactivity.BaseActivity
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.R
import com.trade.tradewise.activity.LoginActivity
import com.trade.tradewise.databinding.ActivitySignUpBinding
import org.json.JSONObject
import java.util.regex.Pattern

class SignUpActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvLogin.setOnClickListener {

            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener {

            if(validate())
            {
                binding.progressBar.visibility=View.VISIBLE
                callApi()
            }
        }




    }

    fun validate():Boolean
    {
        if(binding.etEmail.text.isNullOrBlank() )
        {
            showToast(this,"Please Enter Email !",)
            return  false

        }
        else if(!isValidEmail(binding.etEmail.text.toString()))
        {
            showToast(this,"Please Enter Valid Email !",)
            return  false

        }
        else if(binding.etPassword.text.isNullOrBlank())
        {
            showToast(this,"Please Enter Password !")
            return  false
        }
        else if(binding.etName.text.isNullOrBlank())
        {
            showToast(this,"Please Enter Name !")
            return  false
        }


        return true
    }

    fun callApi()
    {
        var jsonObject=JSONObject()
        jsonObject.put("EMAIL",binding.etEmail.text!!.trim())
        jsonObject.put("First_Name",binding.etName.text!!.trim())
        jsonObject.put("Last_Name",binding.etName.text!!.trim())
        jsonObject.put("password",binding.etPassword.text!!.trim())
        jsonObject.put("password2",binding.etPassword.text!!.trim())
        Log.d("xxx",jsonObject.toString())
        callWeb(Constance.POST,Url.SIGNUP,jsonObject.toString(),object :Retrofitresponse{
            override fun onSucess(response: String?, error: String?) {
                if(response!=null)
                {
                    binding.progressBar.visibility=View.GONE
                    Log.d("respomse",response.toString())
//                    var jsonObject=JSONObject(response)
//                    Log.d("Token",jsonObject.getString("message"))
//                    TokenProvider.setToken(jsonObject.getString("message"))




                }
                else{
                    binding.progressBar.visibility=View.GONE
                    Log.d("error",error.toString());
                }
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progressBar.visibility=View.GONE
                Log.d("error",error.toString());
//                var jsonObject=JSONObject(response)
                showToast(this@SignUpActivity,response.toString(),)
            }

        })
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}