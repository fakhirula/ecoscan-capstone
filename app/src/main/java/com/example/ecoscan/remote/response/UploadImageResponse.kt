package com.example.ecoscan.remote.response

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Result(
	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("waste_type")
	val wasteType: String,

	@field:SerializedName("filename")
	val filename: String,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("url")
	val url: String
)
