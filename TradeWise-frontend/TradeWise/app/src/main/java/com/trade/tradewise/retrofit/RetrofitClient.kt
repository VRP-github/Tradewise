package com.star.starmedicahub.retrofit

import android.annotation.SuppressLint
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.HashMap
import java.util.concurrent.TimeUnit

object RetrofitClient {

val retrofit:getService @SuppressLint("SuspiciousIndentation")
get() {

    val intercepter = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().apply {
        this.addInterceptor(intercepter)
        this.addInterceptor(AuthInterceptor())
        this.connectTimeout(120, TimeUnit.SECONDS) // Connection timeout
        this.writeTimeout(120, TimeUnit.SECONDS)   // Write timeout
        this.readTimeout(120, TimeUnit.SECONDS)

            // time out setting


    }.build()


      var retrofit=Retrofit.Builder()
          .baseUrl(Constance.BASE_URL)
          .client(client)
          .addConverterFactory(ScalarsConverterFactory.create()).build()



        return  retrofit.create<getService>(getService::class.java)
    }



    interface getService {
        @POST
        fun getPostService(
            @HeaderMap headers: HashMap<String?, String?>?,
            @Url url: String?,
            @Body request: String?
        ): Call<String>

        @GET
        fun getGETService(
            @HeaderMap headers: HashMap<String?, String?>?,
            @Url url: String?,
            @QueryMap queryParams: HashMap<String?, Any?>?
        ): Call<String>


        @DELETE
        fun getDeleteService(
            @HeaderMap headers: HashMap<String?, String?>?,
            @Url url: String?,
            @QueryMap queryParams: HashMap<String?, Any?>?
        ): Call<String>

        @Multipart
        @POST
        fun requestWithFiles(
            @HeaderMap headers: HashMap<String?, String?>?,
            @Url url: String?,
            @PartMap parts: Map<String?, String?>,
//            @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>?,
            @Part files: List<MultipartBody.Part?>?
        ): Call<String>

    }


}





