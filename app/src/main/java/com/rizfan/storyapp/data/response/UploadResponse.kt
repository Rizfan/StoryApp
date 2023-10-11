package com.rizfan.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
