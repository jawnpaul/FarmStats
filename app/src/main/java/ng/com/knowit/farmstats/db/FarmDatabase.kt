package ng.com.knowit.farmstats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ng.com.knowit.farmstats.model.Farm

@Database(entities = arrayOf(Farm::class), version = 1)
@TypeConverters(LatLngListConverter::class)
abstract class FarmDatabase : RoomDatabase() {

    object DatabaseProvider {

        private lateinit var INSTANCE: FarmDatabase
        fun getDatabase(context: Context): FarmDatabase {
            synchronized(FarmDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FarmDatabase::class.java,
                        "farm_database"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun farmDao(): FarmDao
}