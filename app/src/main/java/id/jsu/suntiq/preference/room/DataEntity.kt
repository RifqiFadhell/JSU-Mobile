package id.jsu.suntiq.preference.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "vehicle")
data class DataEntity(
    @ColumnInfo(name = "policeNumber") var policeNumber: String,
    @ColumnInfo(name = "id_vehicle") var idVehicle: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "chassingNumber") var chassingNumber: String,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
) : Parcelable