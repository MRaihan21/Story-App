package com.rad21.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class AddNewResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
