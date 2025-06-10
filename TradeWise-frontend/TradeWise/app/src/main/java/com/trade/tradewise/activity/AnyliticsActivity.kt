package com.trade.tradewise.activity

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.star.starmedicahub.baseactivity.BaseActivity
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.databinding.ActivityAnyliticsBinding
import com.trade.tradewise.model.AccountModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AnyliticsActivity : BaseActivity() {
    lateinit var binding: ActivityAnyliticsBinding
    lateinit var accountList: List<AccountModel>
    lateinit var accountList1: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnyliticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callApi()

        val openDatePicker = {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

            picker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

            picker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                val formattedDate = sdf.format(Date(selection))
                binding.dateInput.setText(formattedDate)
            }
        }

        binding.dateInput.setOnClickListener { openDatePicker() }
        binding.calendarIcon.setOnClickListener { openDatePicker() }

        binding.downloadButton.setOnClickListener {
            showFileTypeDialog()
        }
    }

    private fun showFileTypeDialog() {
        val options = arrayOf("PDF", "XLSX", "CSV")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select File Type")
        builder.setItems(options) { dialog, which ->
            val selectedType = when (which) {
                0 -> "pdf"
                1 -> "xlsx"
                2 -> "csv"
                else -> "pdf"
            }
            downloadFile(
                this,
                TokenProvider.getUserId().toString(),
                TokenProvider.getAccountId(),
                binding.stockDropdown.text.toString(),
                binding.dateInput.text.toString(),
                getTodayFormattedDate(),
                selectedType
            )
        }
        builder.show()
    }

    fun setNameAdtper(accountList1: List<AccountModel>) {
        val accountNames = accountList1.map { it.aCCOUNTNAME }
        binding.accountDropdown.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, accountNames)
        )
        binding.accountDropdown.setOnItemClickListener { parent, view, position, id ->
            val selectedAccount = accountList[position]
            val selectedId = selectedAccount.aCCOUNTID
            callApi(selectedId.toString())
            Toast.makeText(this, "Selected ID: $selectedId", Toast.LENGTH_SHORT).show()
        }
    }

    fun setNameAdtpers(accountList1: List<String>) {
        binding.stockDropdown.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, accountList1)
        )
        binding.stockDropdown.setOnItemClickListener { parent, view, position, id ->
            val selectedAccount = accountList1[position]
            Toast.makeText(this, "Selected ID: $selectedAccount", Toast.LENGTH_SHORT).show()
        }
    }

    fun callApi() {
        binding.progressBar.visibility = View.VISIBLE
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", TokenProvider.getUserId())

        callWeb(Constance.POST, Url.ACCOUNT_LIST, jsonObject.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                if (response != null) {
                    binding.progressBar.visibility = View.GONE
                    val jsonObject = JSONObject(response)
                    accountList = Gson().fromJson(
                        jsonObject.getJSONArray("accounts").toString(),
                        object : TypeToken<List<AccountModel>>() {}.type
                    )
                    setNameAdtper(accountList)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Log.d("error", error.toString())
                }
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progressBar.visibility = View.GONE
                Log.d("error", error.toString())
                showToast(this@AnyliticsActivity, response.toString())
            }
        })
    }

    fun callApi(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", TokenProvider.getUserId())
        jsonObject.put("account_id", id)

        callWeb(Constance.POST, Url.ANALYTICS_REPORT, jsonObject.toString(), object : Retrofitresponse {
            override fun onSucess(response: String?, error: String?) {
                if (response != null) {
                    binding.progressBar.visibility = View.GONE
                    val jsonObject = JSONObject(response)
                    accountList1 = Gson().fromJson(
                        jsonObject.getJSONArray("tickers").toString(),
                        object : TypeToken<List<String>>() {}.type
                    )
                    setNameAdtpers(accountList1)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Log.d("error", error.toString())
                }
            }

            override fun onFailer(response: String?, error: String?) {
                binding.progressBar.visibility = View.GONE
                Log.d("error", error.toString())
                showToast(this@AnyliticsActivity, response.toString())
            }
        })
    }

    fun getTodayFormattedDate(): String {
        val today = Calendar.getInstance().time
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(today)
    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormat = if (inputDate.contains("/")) {
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        }
        val date = inputFormat.parse(inputDate)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(date)
    }

    fun downloadFile(
        context: Context,
        customerId: String,
        accountId: String,
        ticker: String,
        startDate: String,
        endDate: String,
        fileType: String
    ) {
        val formattedStartDate = convertDateFormat(startDate)
        val formattedEndDate = convertDateFormat(endDate)

        val url = "https://tradewise-backend-14.onrender.com/analytics_report/fetch_download_data/?customer_id=$customerId&account_id=$accountId&ticker=$ticker&start_date=$formattedStartDate&end_date=$formattedEndDate&file_type=$fileType"
        Log.d("DownloadURL", url)

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Download Report")
            .setDescription("Downloading analytics report...")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "report_$formattedEndDate.$fileType"
            )
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}
