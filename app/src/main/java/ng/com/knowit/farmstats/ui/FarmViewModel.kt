package ng.com.knowit.farmstats.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ng.com.knowit.farmstats.db.FarmDatabase.DatabaseProvider.getDatabase
import ng.com.knowit.farmstats.db.FarmRepository

class FarmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FarmRepository(getDatabase(application))

    fun getAllFarms() = repository.getAllFarms()
}