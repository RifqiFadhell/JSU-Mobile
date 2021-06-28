package id.jsu.suntiq.api.response.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: DataRegister? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataRegister(

	@field:SerializedName("place_of_birth")
	val placeOfBirth: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("bank")
	val bank: String? = null,

	@field:SerializedName("bank_account_number")
	val bankAccountNumber: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("imei")
	val imei: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("register_date")
	val registerDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
