package com.star.starmedicahub.retrofit

interface Retrofitresponse {
    fun  onSucess(response: String?, error: String?)
    fun  onFailer(response: String?, error: String?)
}