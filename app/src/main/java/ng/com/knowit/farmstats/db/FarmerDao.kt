package ng.com.knowit.farmstats.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ng.com.knowit.farmstats.model.Farmer

@Dao
interface FarmerDao {
    @Insert
    fun insert(farmer: Farmer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(farmers: List<Farmer>)

    @Update
    fun update(farmer: Farmer)

    @Delete
    fun delete(farmer: Farmer)

    @Query("SELECT * FROM farmer_table ORDER BY farmerLocalId ASC")
    fun getAllFarmers(): LiveData<List<Farmer>>

    @Query("SELECT * FROM farmer_table ORDER BY farmerLocalId ASC")
    fun getAllFarmerList(): List<Farmer>
}