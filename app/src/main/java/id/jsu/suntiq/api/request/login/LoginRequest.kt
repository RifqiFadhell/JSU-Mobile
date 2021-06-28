package id.jsu.suntiq.api.response.request

data class LoginRequest(
    var phone_number: String? = "",
    var password: String? = ""
)
