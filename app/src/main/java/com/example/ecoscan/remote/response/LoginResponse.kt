package com.example.ecoscan.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("userDetail")
	val userDetail: UserDetail,

	@field:SerializedName("message")
	val message: String
)

data class UserDetail(

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)
