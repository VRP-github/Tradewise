package com.trade.tradewise.model

import com.google.gson.annotations.SerializedName

data class ProfileOrginizationModel(

	@field:SerializedName("CREATED_BY")
	val cREATEDBY: String? = null,

	@field:SerializedName("LAST_UPDATED_BY")
	val lASTUPDATEDBY: String? = null,

	@field:SerializedName("BALANCE_DESCRIPTION")
	val bALANCEDESCRIPTION: String? = null,

	@field:SerializedName("STOCK_TYPE")
	val sTOCKTYPE: String? = null,

	@field:SerializedName("ACCOUNT_ID")
	val aCCOUNTID: String? = null,

	@field:SerializedName("PROFIT_CALCULATION_METHOD")
	val pROFITCALCULATIONMETHOD: String? = null,

	@field:SerializedName("CUSTOMER_ID")
	val cUSTOMERID: String? = null,

	@field:SerializedName("IS_ACTIVE")
	val iSACTIVE: String? = null,

	@field:SerializedName("LAST_UPDATED_DATE")
	val lASTUPDATEDDATE: String? = null,

	@field:SerializedName("BALANCE_DATE")
	val bALANCEDATE: String? = null,

	@field:SerializedName("ACCOUNT_NAME")
	val aCCOUNTNAME: String? = null,

	@field:SerializedName("CREATE_DATE")
	val cREATEDATE: String? = null,

	@field:SerializedName("INITIAL_DEPOSIT")
	val iNITIALDEPOSIT: String? = null,

	@field:SerializedName("BALANCE_TIME")
	val bALANCETIME: String? = null
)
