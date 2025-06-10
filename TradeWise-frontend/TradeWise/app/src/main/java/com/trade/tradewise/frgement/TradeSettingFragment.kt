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
import com.trade.tradewise.databinding.FragmentSettings2Binding
import com.trade.tradewise.model.AccountModel
import org.json.JSONObject

class TradeSettingFragment : Fragment() {

    private var _binding: FragmentSettings2Binding? = null
    private val binding get() = _binding!!

    private lateinit var accountList: List<AccountModel>
    private var selectedAccountId: String? = null
    private var isExistingEntry: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettings2Binding.inflate(inflater, container, false)
        setupViews()
        fetchAccounts()
        return binding.root
    }

    private fun setupViews() {
        binding.saveButton.setOnClickListener {
            if (validateInputs()) {
                if (isExistingEntry) {
                    editSettings()
                } else {
                    saveSettings()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        return when {
            binding.editFrom.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Please enter From value", Toast.LENGTH_SHORT).show()
                false
            }
            binding.editTo.text.isNullOrEmpty() -> {
                Toast.makeText(requireContext(), "Please enter To value", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun saveSettings() {
        val jsonObject = JSONObject().apply {
            put("customer_id", TokenProvider.getUserId())
            put("account_id", selectedAccountId ?: TokenProvider.getAccountId())
            put("type", "forex")
            put("from_field", binding.editFrom.text.toString())
            put("to_field", binding.editTo.text.toString())
        }

        (activity as? HomeActivity)?.callWeb(
            Constance.POST,
            "https://tradewise-backend-14.onrender.com/general_setting/tradesettings/create/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    Toast.makeText(requireContext(), response ?: "Settings Saved!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailer(response: String?, error: String?) {
                    Toast.makeText(requireContext(), response ?: error ?: "Save Failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun editSettings() {
        val jsonObject = JSONObject().apply {
            put("customer_id", TokenProvider.getUserId())
            put("account_id", selectedAccountId ?: TokenProvider.getAccountId())
            put("from_field", binding.editFrom.text.toString())
            put("to_field", binding.editTo.text.toString())
        }

        (activity as? HomeActivity)?.callWeb(
            Constance.POST,
            "https://tradewise-backend-14.onrender.com/general_setting/tradesettings/edit/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    Toast.makeText(requireContext(), response ?: "Settings Updated!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailer(response: String?, error: String?) {
                    Toast.makeText(requireContext(), response ?: error ?: "Update Failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
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
                    if (response != null) parseAccountResponse(response)
                }
                override fun onFailer(response: String?, error: String?) {
                    Toast.makeText(requireContext(), error ?: "Fetch accounts failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun fetchTradeSetting(accountId: String) {
        val jsonObject = JSONObject().apply {
            put("customer_id", TokenProvider.getUserId())
            put("account_id", accountId)
        }

        (activity as? HomeActivity)?.callWeb(
            Constance.POST,
            "https://tradewise-backend-14.onrender.com/general_setting/tradesettings/get/",
            jsonObject.toString(),
            object : Retrofitresponse {
                override fun onSucess(response: String?, error: String?) {
                    if (!response.isNullOrEmpty()) {
                        try {
                            val json = JSONObject(response)
                            binding.editFrom.setText(json.optString("from_field", ""))
                            binding.editTo.setText(json.optString("to_field", ""))
                            isExistingEntry = true
                        } catch (e: Exception) {
                            isExistingEntry = false
                        }
                    } else {
                        isExistingEntry = false
                        binding.editFrom.setText("")
                        binding.editTo.setText("")
                    }
                }

                override fun onFailer(response: String?, error: String?) {
                    isExistingEntry = false
                    binding.editFrom.setText("")
                    binding.editTo.setText("")
                }
            }
        )
    }

    private fun parseAccountResponse(response: String) {
        val json = JSONObject(response)
        accountList = Gson().fromJson(json.getJSONArray("accounts").toString(), object : TypeToken<List<AccountModel>>() {}.type)

        if (accountList.isNotEmpty()) {
            val accountNames = accountList.map { it.aCCOUNTNAME ?: "" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, accountNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAccount.adapter = adapter

            binding.spinnerAccount.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedAccountId = accountList[position].aCCOUNTID
                    selectedAccountId?.let { fetchTradeSetting(it) }
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                    selectedAccountId = null
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
