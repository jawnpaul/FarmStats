package ng.com.knowit.farmstats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ng.com.knowit.farmstats.model.Farmer

@Database(entities = arrayOf(Farmer::class), version = 2)
abstract class FarmerDatabase : RoomDatabase() {


    object DatabaseProvider {

        private lateinit var INSTANCE: FarmerDatabase
        fun getDatabase(context: Context): FarmerDatabase {
            synchronized(FarmerDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FarmerDatabase::class.java,
                        "farmer_database"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun farmerDao(): FarmerDao
}