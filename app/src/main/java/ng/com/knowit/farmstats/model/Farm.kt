package ng.com.knowit.farmstats.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "farm_table")
data class Farm(
    @ColumnInfo(name = "farmerName") var farmerName: String?,
    @ColumnInfo(name = "farmerLocalId") var farmerLocalId: Int?,
    @ColumnInfo(name = "farmName") var farmName: String?,
    @ColumnInfo(name = "farmerLocation") var farmLocation: String?,
    @ColumnInfo(name = "farmCoordinates") var farmCoordinates: List<LatLng>?
) {

    @PrimaryKey(autoGenerate = true)
    var farmLocalId: Int = 0
}