package id.jsu.suntiq.api.response.login

data class LoginResponse(
	val data: Data? = null,
	val status: Int? = null
)

data class User(
	val place_of_birth: String? = "",
	val imei: String? = "",
	val nik: String? = "",
	val bank: String? = "",
	val bank_account_number: String? = "",
	val date_of_birth: String? = "",
	val phone_number: String? = "",
	val id: Int? = 0,
	val username: String? = "",
	val photo_url: String? = ""
)

data class Data(
	val user: User? = null,
	val token: String? = null
)