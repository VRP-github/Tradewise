package com.trade.tradewise.frgement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.databinding.FragmentSetting1Binding
import com.trade.tradewise.model.AccountModel
import org.json.JSONObject

class CommissionFeesFragment : Fragment() {

    private var _binding: FragmentSetting1Binding? = null
    private val binding get() = _binding!!

    private lateinit var accountList: List<AccountModel>
    private var selectedAccountId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetting1Binding.inflate(inflater, container, false)

        binding.saveButton.setOnClickListener {
            if (validateInputs()) {
                (activity as? HomeActivity)?.let { homeActivity ->
                    callApiUpdateTransaction(homeActivity)
                }
            }
        }

        fetchAccounts()

        return binding.root
    }

    private fun validateInputs(): Boolean {
        return when {
            binding.editComm.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Please enter commission", Toast.LENGTH_SHORT).show()
                false
            }
            binding.editFees.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Please enter fees", Toast.LENGTH_SHORT).show()
                false
            }
            selectedAccountId.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Please select an account", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun fetchAccounts() {
        val jsonObject = JSONObject().apply {
            put("customer_id", TokenProvider.getUserId())
        }

        (activity as? HomeActivity)?.callWeb(
            Constance.POST,
            "https://tradewise-backend-14.onrender.com/analytics_report/fetch-account-names/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    if (response != null) {
                        val json = JSONObject(response)
                        accountList = Gson().fromJson(
                            json.getJSONArray("accounts").toString(),
                            object : TypeToken<List<AccountModel>>() {}.type
                        )
                        setupAccountDropdown(accountList)
                    } else {
                        Log.e("AccountFetch", error ?: "Unknown error")
                    }
                }

                override fun onFailer(response: String?, error: String?) {
                    Toast.makeText(requireContext(), error ?: "Failed to fetch accounts", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun setupAccountDropdown(accounts: List<AccountModel>) {
        val accountNames = accounts.map { it.aCCOUNTNAME ?: "" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, accountNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAccount.adapter = adapter

        binding.spinnerAccount.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAccountId = accounts[position].aCCOUNTID
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                selectedAccountId = null
            }
        })

        // Optional setup for "Apply" spinner if needed
        val applyOptions = listOf("All", "Selected")
        val applyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, applyOptions)
        applyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerApply.adapter = applyAdapter
    }

    private fun callApiUpdateTransaction(homeActivity: HomeActivity) {
        val jsonObject = JSONObject().apply {
            put("customer_id", TokenProvider.getUserId())
            put("account_id", selectedAccountId ?: TokenProvider.getAccountId())
            put("commission", binding.editComm.text.toString())
            put("fees", binding.editFees.text.toString())
        }

        homeActivity.callWeb(
            Constance.POST,
            "https://tradewise-backend-14.onrender.com/comm_fees/update-transactions/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    if (response != null) {
                        homeActivity.showToast(requireActivity(), "Commission and Fees updated successfully!")
                    } else {
                        homeActivity.showToast(requireActivity(), error ?: "Unknown error")
                    }
                }

                override fun onFailer(response: String?, error: String?) {
                    homeActivity.showToast(requireActivity(), response ?: error ?: "Update failed")
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
