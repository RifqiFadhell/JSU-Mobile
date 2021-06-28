package id.jsu.suntiq.api.request.register

data class RegisterRequest(
    var phone_number: String? = "",
    var password: String? = "",
    var fullname: String? = "",
    var nik: String? = "",
    var place_of_birth: String? = "",
    var date_of_birth: String? = "",
    var bank: String? = "",
    var bank_account_number: String? = "",
    var imei: String? = ""
)
