package com.trade.tradewise.frgement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonObject
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.R
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.databinding.FragmentChangePasswordFrgementBinding
import org.json.JSONObject


class ChangePasswordFrgement : Fragment() {

    private var _binding: FragmentChangePasswordFrgementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordFrgementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example usage
        binding.btnChangePassword.setOnClickListener {
           callApi()
            // Add your password change logic here
        }
    }
    fun callApi()
    {
        binding.progress.visibility=View.VISIBLE
        var activity=activity as HomeActivity
        var json= JSONObject()
        json.put("new_password",binding.etNewPassword.text.toString())
        activity.callWeb(Constance.POST, Url.ChangePassword+"/${TokenProvider.getUserId()}/",json.toString(),object :
            Retrofitresponse{
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility=View.GONE
                Toast.makeText(activity,"Password Change Successfully",Toast.LENGTH_SHORT).show()
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progress.visibility=View.GONE
                Toast.makeText(activity,"Some error",Toast.LENGTH_SHORT).show()

            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
