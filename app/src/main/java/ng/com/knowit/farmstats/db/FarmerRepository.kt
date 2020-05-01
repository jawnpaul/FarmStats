package ng.com.knowit.farmstats.db

import androidx.lifecycle.LiveData
import ng.com.knowit.farmstats.model.Farmer

class FarmerRepository(private val database: FarmerDatabase) {

    fun getAllFarmers(): LiveData<List<Farmer>> {
        return database.farmerDao().getAllFarmers()
    }
}