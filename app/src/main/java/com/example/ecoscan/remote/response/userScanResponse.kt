package com.example.ecoscan.remote.response

import com.google.gson.annotations.SerializedName

data class UserScan10Response(

	@field:SerializedName("listScans")
	val listScans: List<ListScansItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ListScansItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("waste_type")
	val wasteType: String,

	@field:SerializedName("filename")
	val filename: String,

	@field:SerializedName("attachment")
	val attachment: String,

	@field:SerializedName("users_id")
	val usersId: Int,

	@field:SerializedName("id")
	val id: Int
)
