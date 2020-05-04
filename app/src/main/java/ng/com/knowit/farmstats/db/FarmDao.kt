package ng.com.knowit.farmstats.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ng.com.knowit.farmstats.model.Farm

@Dao
interface FarmDao {

    @Insert
    fun insert(farm: Farm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(farms: List<Farm>)

    @Update
    fun update(farm: Farm)

    @Delete
    fun delete(farm: Farm)

    @Query("SELECT * FROM farm_table ORDER BY farmLocalId ASC")
    fun getAllFarms(): LiveData<List<Farm>>

    @Query("SELECT * FROM farm_table ORDER BY farmLocalId ASC")
    fun getAllFarmsList(): List<Farm>
}