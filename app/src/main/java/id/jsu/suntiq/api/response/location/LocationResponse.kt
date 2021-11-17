package id.jsu.suntiq.api.response.location

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationResponse(

	@field:SerializedName("data")
	val data: List<LocationItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
): Serializable

data class LocationItem(

	@field:SerializedName("pic_contact")
	val picContact: String? = null,

	@field:SerializedName("branch_pic")
	val branchPic: String? = null,

	@field:SerializedName("office_name")
	val officeName: String? = null,

	@field:SerializedName("coordinate")
	val coordinate: String? = null,

	@field:SerializedName("branch_address")
	val branchAddress: String? = null,

	@field:SerializedName("distance_in_km")
	val distanceInKm: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("branch")
	val branch: String? = null
): Serializable
