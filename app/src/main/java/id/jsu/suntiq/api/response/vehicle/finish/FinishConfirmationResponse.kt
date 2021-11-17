package id.jsu.suntiq.api.response.vehicle.finish

import com.google.gson.annotations.SerializedName

data class FinishConfirmationResponse(

	@field:SerializedName("data")
	val data: DataFinish? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataFinish(

	@field:SerializedName("vehicle_side_d_picture_url")
	val vehicleSideDPictureUrl: String? = null,

	@field:SerializedName("recipient_picture")
	val recipientPicture: String? = null,

	@field:SerializedName("recipient_office_picture_url")
	val recipientOfficePictureUrl: String? = null,

	@field:SerializedName("vehicle_number_picture_url")
	val vehicleNumberPictureUrl: String? = null,

	@field:SerializedName("vehicle_side_b_picture_url")
	val vehicleSideBPictureUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("vehicle_side_a_picture_url")
	val vehicleSideAPictureUrl: String? = null,

	@field:SerializedName("vehicle_side_c_picture_url")
	val vehicleSideCPictureUrl: String? = null,

	@field:SerializedName("bastk_picture_url")
	val bastkPictureUrl: String? = null,

	@field:SerializedName("recipient_position")
	val recipientPosition: String? = null,

	@field:SerializedName("vehicle_side_b_picture")
	val vehicleSideBPicture: String? = null,

	@field:SerializedName("recipient_office_picture")
	val recipientOfficePicture: String? = null,

	@field:SerializedName("vehicle_side_a_picture")
	val vehicleSideAPicture: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("group_picture_url")
	val groupPictureUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("serah_terima_picture_url")
	val serahTerimaPictureUrl: String? = null,

	@field:SerializedName("group_picture")
	val groupPicture: String? = null,

	@field:SerializedName("recipient_picture_url")
	val recipientPictureUrl: String? = null,

	@field:SerializedName("tanda_tangan_bastk_picture")
	val tandaTanganBastkPicture: String? = null,

	@field:SerializedName("vehicle_number_picture")
	val vehicleNumberPicture: String? = null,

	@field:SerializedName("serah_terima_picture")
	val serahTerimaPicture: String? = null,

	@field:SerializedName("tanda_tangan_bastk_picture_url")
	val tandaTanganBastkPictureUrl: String? = null,

	@field:SerializedName("vehicle_side_d_picture")
	val vehicleSideDPicture: String? = null,

	@field:SerializedName("vehicle_side_c_picture")
	val vehicleSideCPicture: String? = null,

	@field:SerializedName("assignment_id")
	val assignmentId: Int? = null,

	@field:SerializedName("recipient_phone_number")
	val recipientPhoneNumber: String? = null,

	@field:SerializedName("recipient_name")
	val recipientName: String? = null,

	@field:SerializedName("bastk_picture")
	val bastkPicture: String? = null
)
