package id.jsu.suntiq.api.response.vehicle

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetailVehicleResponse(

	@field:SerializedName("data")
	val data: DetailVehicle? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Leasing(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("leasing_code")
	val leasingCode: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("leasing_name")
	val leasingName: String? = null
): Serializable

data class DetailVehicle(

	@field:SerializedName("police_number")
	val policeNumber: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("year")
	val year: Any? = null,

	@field:SerializedName("stnk")
	val stnk: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("chassing_number")
	val chassingNumber: String? = null,

	@field:SerializedName("leasing")
	val leasing: Leasing? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("machine_number")
	val machineNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("status")
	val status: Any? = null
): Serializable
