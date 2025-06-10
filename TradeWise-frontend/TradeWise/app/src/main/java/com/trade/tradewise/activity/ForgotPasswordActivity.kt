package com.trade.tradewise.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.trade.tradewise.databinding.ActivityForgotPasswordBinding
import com.star.starmedicahub.baseactivity.BaseActivity
import org.json.JSONObject

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangePassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()

            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            } else {
                callForgotPasswordApi(email, newPassword)
            }
        }
    }

    private fun callForgotPasswordApi(email: String, newPassword: String) {
        binding.progressBar.visibility = View.VISIBLE

        val jsonObject = JSONObject().apply {
            put("email", email)
            put("new_password", newPassword)
        }

        callWeb(
            Constance.POST,
            "https://tradewise-backend-14.onrender.com/auth_login/recover_account/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    binding.progressBar.visibility = View.GONE
                    if (response != null) {
                        Toast.makeText(this@ForgotPasswordActivity, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ForgotPasswordActivity, error ?: "Unknown error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailer(response: String?, error: String?) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ForgotPasswordActivity, response ?: error ?: "Failed to reset password", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
