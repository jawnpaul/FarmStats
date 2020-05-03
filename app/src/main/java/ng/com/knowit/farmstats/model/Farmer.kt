package ng.com.knowit.farmstats.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_table")
data class Farmer(
    @ColumnInfo(name = "farmerFirstName") var farmerFirstName: String?,
    @ColumnInfo(name = "farmerLastName") var farmerLastName: String?,
    @ColumnInfo(name = "farmerAddress") var farmerAddress: String?,
    @ColumnInfo(name = "farmerPhoneNumber") var farmerPhoneNumber: String?,
    @ColumnInfo(name = "farmerPhotoUri") var farmerPhotoUri: String?,
    @ColumnInfo(name = "farmerFullName") var farmerFullName: String?,
    @ColumnInfo(name = "farmerDateCreated") var farmerDateCreated: String?
) {

    @PrimaryKey(autoGenerate = true)
    var farmerLocalId: Int = 0
}