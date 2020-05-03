package ng.com.knowit.farmstats.db

import androidx.lifecycle.LiveData
import ng.com.knowit.farmstats.model.Farm

class FarmRepository(private val database: FarmDatabase) {

    fun getAllFarms(): LiveData<List<Farm>> {
        return database.farmDao().getAllFarms()
    }
}