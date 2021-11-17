package id.jsu.suntiq.api.request.vehicle.confirmation

import java.io.File

data class ConfirmationRequest(
    var user_id: String? = null,
    var from_jsu: Boolean? = null,
    var vehicle_id: String? = null,
    var receiving_office_id: String? = null,
    var vehicle_side_a_picture: File? = null,
    var vehicle_side_b_picture: File? = null,
    var vehicle_registration_picture: File? = null
)

data class ConfirmationOfficeRequest(
    var assignment_id: String? = null,
    var recipient_name: String? = null,
    var recipient_phone_number: String? = null,
    var recipient_position: String? = null,
    var recipient_office_picture: File? = null,
    var recipient_picture: File? = null,
    var vehicle_number_picture: File? = null
)
data class FinishConfirmationRequest(
    var assignment_id: String? = null,
    var serah_terima_picture: File? = null,
    var vehicle_side_a_picture: File? = null,
    var vehicle_side_b_picture: File? = null,
    var vehicle_side_c_picture: File? = null,
    var vehicle_side_d_picture: File? = null,
    var tanda_tangan_bastk_picture: File? = null,
    var bastk_picture: File? = null,
    var group_picture: File? = null
)

