package com.trade.tradewise.frgement

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.Retrofitresponse
import com.star.starmedicahub.retrofit.TokenProvider
import com.star.starmedicahub.retrofit.Url
import com.trade.tradewise.R
import com.trade.tradewise.activity.HomeActivity
import com.trade.tradewise.databinding.FragmentTradeLogBinding
import org.json.JSONObject


class TradeLogFragment : Fragment() {

lateinit var _binding:FragmentTradeLogBinding
    private val binding get() = _binding!!
    lateinit var fileUri: Uri

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            fileUri=uri
        }
        uri?.let {
            val fileType = activity?.contentResolver?.getType(it) // Get MIME type
            Log.d("FilePicker", "Selected file URI: $it")
            Log.d("FilePicker", "Selected file type: $fileType")
            val fileName = getFileName(it)
            binding.image.visibility= View.GONE
            binding.fileName.visibility= View.VISIBLE
            binding.fileName.text=fileName

            // Handle the selected file
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentTradeLogBinding.inflate(layoutInflater, container, false)

        binding.btnAddFIle.setOnClickListener{

            filePickerLauncher.launch(arrayOf("application/pdf", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","text/csv"))
        }
        binding.btnUpload.setOnClickListener {

            callAPi()
        }
        return  binding.root
    }

    fun callAPi()
    {
         binding.progress.visibility= View.VISIBLE
        var jsonObject= JSONObject()
     var context=activity as HomeActivity
        context.callForms(context, Url.UPLOADFILE+"/${TokenProvider.getUserId()}/${TokenProvider.getAccountId()}/",jsonObject.toString(),listOf(fileUri),listOf("file"),object :
            Retrofitresponse{
            override fun onSucess(response: String?, error: String?) {
                Toast.makeText(context,response, Toast.LENGTH_SHORT).show()
                binding.progress.visibility= View.GONE
            }

            override fun onFailer(response: String?, error: String?) {
                Toast.makeText(context,response, Toast.LENGTH_SHORT).show()
                binding.progress.visibility= View.GONE
            }

        })
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }


}