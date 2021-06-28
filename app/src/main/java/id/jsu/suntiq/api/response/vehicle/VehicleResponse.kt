package id.jsu.suntiq.api.response.vehicle

import com.google.gson.annotations.SerializedName

data class VehicleResponse(

	@field:SerializedName("data")
	val data: List<DataVehicle>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataVehicle(

	@field:SerializedName("police_number")
	val policeNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("chassing_number")
	val chassingNumber: String? = null
)
