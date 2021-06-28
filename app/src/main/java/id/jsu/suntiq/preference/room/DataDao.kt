package id.fadhell.testpayfazz.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAllNews(): Flowable<List<NewsEntity>>

    @Insert(onConflict = REPLACE)
    fun insertNews(news: List<NewsEntity>)

    @Query("DELETE from news")
    fun deleteAll()
}