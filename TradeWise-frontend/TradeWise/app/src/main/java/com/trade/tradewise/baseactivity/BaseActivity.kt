package com.star.starmedicahub.baseactivity

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.star.starmedicahub.retrofit.Constance
import com.star.starmedicahub.retrofit.RetrofitClient
import com.star.starmedicahub.retrofit.Retrofitresponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException

open class BaseActivity : AppCompatActivity() {

    fun callWeb(mainMethodType: String, url: String, body: String, callback: Retrofitresponse) {
        val headers = HashMap<String?, String?>()
        val call: Call<String> = when (mainMethodType) {
            Constance.POST -> {
                headers["Content-Type"] = "application/json"
                RetrofitClient.retrofit.getPostService(headers, url, body)
            }

            Constance.GET -> {
                headers["Content-Type"] = "application/json"
                val paramsHashMap = Gson().fromJson<HashMap<String?, Any?>>(
                    body,
                    object : TypeToken<HashMap<String?, String?>?>() {}.type
                )
                Log.d("getparms", paramsHashMap.toString())
                RetrofitClient.retrofit.getGETService(headers, url, paramsHashMap)
            }

            Constance.DELETE -> {
                headers["Content-Type"] = "application/json"
                val paramsHashMap = Gson().fromJson<HashMap<String?, Any?>>(
                    body,
                    object : TypeToken<HashMap<String?, String?>?>() {}.type
                )
                Log.d("getparms", paramsHashMap.toString())
                RetrofitClient.retrofit.getDeleteService(headers, url, paramsHashMap)
            }

            else -> {
                throw IllegalArgumentException("Unsupported method type: $mainMethodType")
            }
        }

        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback.onSucess(response.body().toString(), response.errorBody()?.toString())
                } else {
                    val errBody = response.errorBody()?.string().orEmpty()
                    Log.d("responserr", errBody)
                    callback.onFailer(errBody, errBody)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "No internet connection"
                    is HttpException -> "Something went wrong!"
                    else -> t.localizedMessage ?: "Unknown error"
                }
                callback.onFailer(errorMessage, "not")
            }
        })
    }

    fun callForms(
        context: Context,
        url: String,
        body: String,
        uris: List<Uri>,
        filekey: List<String>,
        callback: Retrofitresponse
    ) {
        val headers = HashMap<String?, String?>()
        val paramsHashMap = Gson().fromJson<HashMap<String?, String?>>(
            body,
            object : TypeToken<HashMap<String?, String?>?>() {}.type
        )

        val multipartFiles = uris.mapIndexed { index, uri ->
            val file = uriToFile(context, uri)
            MultipartBody.Part.createFormData(filekey[index], file.name, file.asRequestBody())
        }

        val call = RetrofitClient.retrofit.requestWithFiles(headers, url, paramsHashMap, multipartFiles)
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful)
                    callback.onSucess(response.body().toString(), response.errorBody()?.toString())
                else {
                    val errBody = response.errorBody()?.string().orEmpty()
                    Log.d("responserr", errBody)
                    callback.onFailer(errBody, errBody)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onFailer(t.toString(), "not")
            }
        })
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val file = File(context.cacheDir, getFileName(context, uri))
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }

    fun getFileName(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex("_display_name")
                if (nameIndex != -1) {
                    return it.getString(nameIndex)
                }
            }
        }
        return uri.lastPathSegment ?: "unknown"
    }
}
