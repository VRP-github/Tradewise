package com.trade.tradewise.frgement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.databinding.FragmentDashboardBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    data class PriceData(val date: String, val price: Float)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        callAPi()
        profitloss()
        profitFactor()
        winorlost()
        fetchChartData()

        return binding.root
    }

    private fun fetchChartData() {
        binding.progress.visibility = View.VISIBLE
        val activity = activity as HomeActivity
        val customerId = TokenProvider.getUserId()
        val accountId = TokenProvider.getAccountId()

        Log.d("API_REQUEST", "Calling TransactionGraph API\ncustomer_id=$customerId\naccount_id=$accountId")

        val json = JSONObject()
        json.put("customer_id", customerId)
        json.put("account_id", accountId)

        activity.callWeb(Constance.POST, "https://tradewise-backend-14.onrender.com/dashboard/transaction_graph/", json.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.d("API_SUCCESS", "TransactionGraph Response: $response")

                val priceDataList = ArrayList<PriceData>()

                try {
                    val jsonArray = JSONObject(response).getJSONArray("data") // Assuming "data" key holds array

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val date = item.getString("date")   // Expecting a field named "date"
                        val price = item.getDouble("price").toFloat() // Expecting a field named "price"
                        priceDataList.add(PriceData(date, price))
                    }

                    // Now bind the data to the chart
                    setupChart(priceDataList)

                } catch (e: Exception) {
                    Log.e("API_PARSE_ERROR", "Error parsing TransactionGraph data: ${e.message}")
                }
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.e("API_FAILURE", "TransactionGraph Error: $error\nResponse: $response")
            }
        })
    }

    private fun setupChart(priceDataList: List<PriceData>) {
        val dataPoints = priceDataList.map { it.price }
        val labels = priceDataList.map { formatDate(it.date) }

        binding.customChart.dataPoints = dataPoints
        binding.customChart.labels = labels
        binding.customChart.invalidate()
    }

    // Helper function to format "yyyy-MM-dd" to "dd MMM"
    private fun formatDate(inputDate: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
            val date = parser.parse(inputDate)
            if (date != null) formatter.format(date) else inputDate
        } catch (e: Exception) {
            inputDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callAPi() {
        binding.progress.visibility = View.VISIBLE
        val activity = activity as HomeActivity
        val customerId = TokenProvider.getUserId()
        val accountId = TokenProvider.getAccountId()

        Log.d("API_REQUEST", "Calling DashboardWin API\ncustomer_id=$customerId\naccount_id=$accountId")

        val json = JSONObject()
        json.put("customer_id", customerId)
        json.put("account_id", accountId)

        activity.callWeb(Constance.POST, Url.DASHBOARDWIN, json.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.d("API_SUCCESS", "DashboardWin Response: $response")
                val jsonResponse = JSONObject(response)
                binding.tradewun.text = "${jsonResponse.getDouble("profit_percentage")}%"
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.e("API_FAILURE", "DashboardWin Error: $error\nResponse: $response")
            }
        })
    }

    private fun profitloss() {
        binding.progress.visibility = View.VISIBLE
        val activity = activity as HomeActivity
        val customerId = TokenProvider.getUserId()
        val accountId = TokenProvider.getAccountId()

        Log.d("API_REQUEST", "Calling ProfitLoss API\ncustomer_id=$customerId\naccount_id=$accountId")

        val json = JSONObject()
        json.put("customer_id", customerId)
        json.put("account_id", accountId)

        activity.callWeb(Constance.POST, Url.Profitloss, json.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.d("API_SUCCESS", "ProfitLoss Response: $response")
                val jsonResponse = JSONObject(response)
                val profitLoss = jsonResponse.getDouble("total_profit_loss")
                binding.npl.text = "$${String.format("%.2f", profitLoss)}"
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.e("API_FAILURE", "ProfitLoss Error: $error\nResponse: $response")
            }
        })
    }

    private fun profitFactor() {
        binding.progress.visibility = View.VISIBLE
        val activity = activity as HomeActivity
        val customerId = TokenProvider.getUserId()
        val accountId = TokenProvider.getAccountId()

        Log.d("API_REQUEST", "Calling ProfitFactor API\ncustomer_id=$customerId\naccount_id=$accountId")

        val json = JSONObject()
        json.put("customer_id", customerId)
        json.put("account_id", accountId)

        activity.callWeb(Constance.POST, Url.ProfitFactor, json.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.d("API_SUCCESS", "ProfitFactor Response: $response")
                val jsonResponse = JSONObject(response)
                binding.tvpf.text = String.format("%.2f", jsonResponse.getDouble("profit_factor"))
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.e("API_FAILURE", "ProfitFactor Error: $error\nResponse: $response")
            }
        })
    }

    private fun winorlost() {
        binding.progress.visibility = View.VISIBLE
        val activity = activity as HomeActivity
        val customerId = TokenProvider.getUserId()
        val accountId = TokenProvider.getAccountId()

        Log.d("API_REQUEST", "Calling WinOrLoss API\ncustomer_id=$customerId\naccount_id=$accountId")

        val json = JSONObject()
        json.put("customer_id", customerId)
        json.put("account_id", accountId)

        activity.callWeb(Constance.POST, Url.AVG_WIN_LOSS, json.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.d("API_SUCCESS", "WinOrLoss Response: $response")
                val jsonResponse = JSONObject(response)
                val avgWinLoss = jsonResponse.getDouble("avg_win_loss_ratio")
                binding.prfoitlost.text = String.format("%.2f", avgWinLoss)
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progress.visibility = View.GONE
                Log.e("API_FAILURE", "WinOrLoss Error: $error\nResponse: $response")
            }
        })
    }
}
