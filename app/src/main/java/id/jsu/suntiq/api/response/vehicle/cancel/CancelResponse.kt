package id.jsu.suntiq.api.response.vehicle.cancel

import com.google.gson.annotations.SerializedName

data class CancelResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Docs(

	@field:SerializedName("surat_jalan")
	val suratJalan: String? = null
)

data class Data(

	@field:SerializedName("doc_url")
	val docUrl: Any? = null,

	@field:SerializedName("from_jsu")
	val fromJsu: Int? = null,

	@field:SerializedName("receiving_office_id")
	val receivingOfficeId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("docs")
	val docs: Docs? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("cancellation_cause")
	val cancellationCause: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("vehicle_id")
	val vehicleId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
