package id.jsu.suntiq.preference.tinyDb.data

import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

data class ConfirmationData(
    @field:SerializedName("id_location")
    val idLocation: Int? = null,

    @field:SerializedName("branch")
    val branch: String? = null
): Serializable
