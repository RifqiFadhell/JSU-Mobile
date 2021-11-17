package id.jsu.suntiq.api.request.vehicle.cancel

data class CancelRequest(
    var assignment_id: String? = "",
    var cancellation_cause: String? = ""
)
