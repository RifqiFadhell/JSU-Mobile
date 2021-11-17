package id.jsu.suntiq.api.response

class DataResponse {
}

data class CompleteResponse(
    var data: CompleteData?,
    var status: Int?,
    var message: String?
)

data class CompleteData(
    var sync_status: String?
)