package id.fadhell.testpayfazz.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "news")
data class NewsEntity(
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "urlToImage") var urlImage: String,
    @ColumnInfo(name = "publishedAt") var publishedAt: String,
    @ColumnInfo(name = "content") var content: String,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
) : Parcelable