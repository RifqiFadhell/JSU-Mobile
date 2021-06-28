package id.jsu.suntiq.api.response.register.verify

import com.google.gson.annotations.SerializedName

data class VerifyResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(
	val any: Any? = null
)
