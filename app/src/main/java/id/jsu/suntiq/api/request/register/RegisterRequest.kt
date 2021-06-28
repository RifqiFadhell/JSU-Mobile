package id.jsu.suntiq.api.request.login

data class LoginRequest(
    var phone_number: String? = "",
    var password: String? = ""
)
