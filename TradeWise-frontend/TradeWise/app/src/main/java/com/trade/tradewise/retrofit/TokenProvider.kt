package com.star.starmedicahub.retrofit

import android.content.Context
import android.content.SharedPreferences

object TokenProvider {
    private const val PREFS_NAME = "my_prefs"
    private const val TOKEN_KEY = "token_key"
    private const val COUNT = "count"
    private const val USERID="userid"
    private const val NAME="name"
    private const val ACCOUNTID="accountid"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Save the token to SharedPreferences
    fun setToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }
    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(USERID, userId).apply()
    }
    fun setName(name: String) {
        sharedPreferences.edit().putString(NAME, name).apply()
    }
    fun getName(): String
    {
        return sharedPreferences.getString(NAME, "") ?: ""
    }
    fun getUserId(): String {
        return sharedPreferences.getString(USERID, "") ?: ""
    }
    fun setAccountId(userId: String) {
        sharedPreferences.edit().putString(ACCOUNTID, userId).apply()
    }
    fun getAccountId(): String {
        return sharedPreferences.getString(ACCOUNTID, "") ?: ""
    }

    // Retrieve the token from SharedPreferences
    fun getToken(): String {
        return sharedPreferences.getString(TOKEN_KEY, "") ?: ""
    }
    fun getCount():Int
    {
        return sharedPreferences.getInt(COUNT, 0) ?:0
    }

    fun clear()
    {
        var c=sharedPreferences.edit()
        c.clear().apply()
    }

    fun setCount(count:Int) {
        sharedPreferences.edit().putInt(COUNT, count).apply()
    }
}
