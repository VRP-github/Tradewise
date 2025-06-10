package com.trade.tradewise.model

import com.google.gson.annotations.SerializedName

data class AccountModel(

	@field:SerializedName("ACCOUNT_NAME")
	val aCCOUNTNAME: String? = null,

	@field:SerializedName("ACCOUNT_ID")
	val aCCOUNTID: String? = null
)
