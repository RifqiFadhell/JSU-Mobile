package id.jsu.suntiq.api.response.login

data class LoginResponse(
	val data: Data? = null,
	val status: Int? = null
)

data class User(
	val placeOfBirth: String? = null,
	val nik: String? = null,
	val bank: String? = null,
	val bankAccountNumber: String? = null,
	val dateOfBirth: String? = null,
	val phoneNumber: String? = null,
	val id: Int? = null,
	val username: String? = null
)

data class Data(
	val user: User? = null,
	val token: String? = null
)