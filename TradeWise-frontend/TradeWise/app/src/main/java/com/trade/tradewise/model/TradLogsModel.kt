package com.trade.tradewise.model

import com.google.gson.annotations.SerializedName

data class TradLogsModel(

	@field:SerializedName("CREATED_BY")
	val cREATEDBY: String? = null,

	@field:SerializedName("CREATE_DATE")
	val cREATEDATE: String? = null,

	@field:SerializedName("ACCOUNT_ID")
	val aCCOUNTID: String? = null,

	@field:SerializedName("DUPLICATE_ENTRIES")
	val dUPLICATEENTRIES: Int? = null,

	@field:SerializedName("CUSTOMER_ID")
	val cUSTOMERID: String? = null,

	@field:SerializedName("TRADE_LOG_ID")
	val tRADELOGID: String? = null,

	@field:SerializedName("UPLOAD_TIME")
	val uPLOADTIME: String? = null,

	@field:SerializedName("TOTAL_ENTRIES")
	val tOTALENTRIES: Int? = null,

	@field:SerializedName("FILE_NAME")
	val fILENAME: String? = null
)
