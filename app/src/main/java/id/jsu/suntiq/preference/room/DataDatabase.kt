package id.jsu.suntiq.preference.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [DataEntity::class], version = 1)
abstract class DataDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {
        private var instance: DataDatabase? = null

        fun getInstance(context: Context): DataDatabase? {
            if (instance == null) {
                synchronized(DataDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DataDatabase::class.java,
                        "vehicle.db"
                    )
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }
}