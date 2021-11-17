package id.jsu.suntiq.api.response.vehicle.delivery

import com.google.gson.annotations.SerializedName

data class ConfirmationDeliveryResponse(

	@field:SerializedName("data")
	val data: DataDelivery? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataDelivery(

	@field:SerializedName("recipient_picture_url")
	val recipientPictureUrl: String? = null,

	@field:SerializedName("recipient_picture")
	val recipientPicture: String? = null,

	@field:SerializedName("recipient_office_picture_url")
	val recipientOfficePictureUrl: String? = null,

	@field:SerializedName("vehicle_number_picture")
	val vehicleNumberPicture: String? = null,

	@field:SerializedName("vehicle_number_picture_url")
	val vehicleNumberPictureUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("recipient_position")
	val recipientPosition: String? = null,

	@field:SerializedName("assignment_id")
	val assignmentId: String? = null,

	@field:SerializedName("recipient_phone_number")
	val recipientPhoneNumber: String? = null,

	@field:SerializedName("recipient_office_picture")
	val recipientOfficePicture: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("recipient_name")
	val recipientName: String? = null
)
