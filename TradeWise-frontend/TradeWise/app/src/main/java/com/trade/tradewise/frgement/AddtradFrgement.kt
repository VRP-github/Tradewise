package com.trade.tradewise.frgement

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.TradeLogsAdapter
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.databinding.FragmentAddtradFrgementBinding
import com.trade.tradewise.model.TradLogsModel
import org.json.JSONObject
import java.lang.reflect.Type

class AddtradFrgement : Fragment() {

    private var _binding: FragmentAddtradFrgementBinding?=null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentAddtradFrgementBinding.inflate(layoutInflater, container, false)

          callApi()

        return  binding.root
    }

    fun callApi()
    {
        binding.progress.visibility=View.VISIBLE
        var context=activity as HomeActivity
        var json= JSONObject()
        json.put("customer_id",TokenProvider.getUserId())
        json.put("account_id",TokenProvider.getAccountId())
        Log.d("xxxx",json.toString())
        context.callWeb(Constance.POST,Url.TRADE_LOGS,json.toString(),object:
            Retrofitresponse{
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility=View.GONE
                var json= JSONObject(response)

                var list= Gson().fromJson<List<TradLogsModel>>(json.getJSONArray("logs").toString(),object :
                    TypeToken<List<TradLogsModel>> () {}.type)
                 setAdpter(list)

            }

            override fun onFailer(response: String?, error: String?) {
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
                binding.progress.visibility=View.GONE
            }


        })

    }

    fun setAdpter(list: List<TradLogsModel>)
    {
        val adapter = TradeLogsAdapter(list)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }


}