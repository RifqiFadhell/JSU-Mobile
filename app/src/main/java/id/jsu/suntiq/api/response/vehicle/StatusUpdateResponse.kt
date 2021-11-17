package id.jsu.suntiq.api.response.vehicle

import com.google.gson.annotations.SerializedName

data class StatusUpdateResponse(

	@field:SerializedName("data")
	val data: DataUpdate? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataUpdate(

	@field:SerializedName("sync_status")
	val syncStatus: String? = null
)
