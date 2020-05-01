package ng.com.knowit.farmstats.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ng.com.knowit.farmstats.db.FarmerDatabase.DatabaseProvider.getDatabase
import ng.com.knowit.farmstats.db.FarmerRepository

class FarmerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FarmerRepository(getDatabase(application))

    fun getAllFarmers() = repository.getAllFarmers()

}