package id.jsu.suntiq.preference.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable

@Dao
interface DataDao {

    @Query("SELECT * FROM vehicle")
    fun getAllData(): Flowable<List<DataEntity>>

    @Query("SELECT * FROM vehicle LIMIT 10")
    fun checkData(): Flowable<List<DataEntity>>

    @Insert(onConflict = REPLACE)
    fun insertData(news: List<DataEntity>)

    @Query("DELETE from vehicle")
    fun deleteAll()

    @Query("SELECT * FROM vehicle WHERE policeNumber LIKE :value LIMIT 25")
    fun searchData(value: String): Flowable<List<DataEntity>>

    @Query("DELETE from vehicle WHERE policeNumber LIKE :value")
    fun deleteData(value: String)
}