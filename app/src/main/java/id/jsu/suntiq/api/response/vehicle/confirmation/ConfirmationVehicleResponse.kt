package id.jsu.suntiq.api.response.vehicle.confirmation

import com.google.gson.annotations.SerializedName

data class ConfirmationVehicleResponse(

	@field:SerializedName("data")
	val data: DataConfirmation? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Docs(

	@field:SerializedName("surat_jalan")
	val suratJalan: String? = null
)

data class PictureItem(

	@field:SerializedName("assignment_id")
	val assignmentId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("meta_value")
	val metaValue: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("meta_key")
	val metaKey: String? = null,

	@field:SerializedName("meta_value_url")
	val metaValueUrl: String? = null,

	@field:SerializedName("meta_label")
	val metaLabel: String? = null
)

data class DataConfirmation(

	@field:SerializedName("from_jsu")
	val fromJsu: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("office")
	val office: Office? = null,

	@field:SerializedName("picture")
	val picture: List<PictureItem?>? = null,

	@field:SerializedName("vehicle")
	val vehicle: Vehicle? = null,

	@field:SerializedName("doc_url")
	val docUrl: String? = null,

	@field:SerializedName("receiving_office_id")
	val receivingOfficeId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("docs")
	val docs: Docs? = null,

	@field:SerializedName("partner")
	val partner: Partner? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("cancellation_cause")
	val cancellationCause: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("vehicle_id")
	val vehicleId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Partner(

	@field:SerializedName("expire_date")
	val expireDate: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("level_id")
	val levelId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status_sync")
	val statusSync: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Office(

	@field:SerializedName("pic_contact")
	val picContact: String? = null,

	@field:SerializedName("branch_pic")
	val branchPic: String? = null,

	@field:SerializedName("office_name")
	val officeName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("branch_address")
	val branchAddress: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("branch")
	val branch: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

data class Vehicle(

	@field:SerializedName("police_number")
	val policeNumber: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("stnk")
	val stnk: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("chassing_number")
	val chassingNumber: String? = null,

	@field:SerializedName("leasing")
	val leasing: String? = null,

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
	val status: String? = null
)
