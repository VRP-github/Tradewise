package com.trade.tradewise

import android.app.Application
import com.star.starmedicahub.retrofit.TokenProvider

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        TokenProvider.init(this)
    }
}