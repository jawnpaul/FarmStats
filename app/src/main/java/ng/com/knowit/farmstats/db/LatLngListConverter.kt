package ng.com.knowit.farmstats.db

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LatLngListConverter {

    @TypeConverter
    fun fromLatLngString(value: String): List<LatLng> {
        val type = object : TypeToken<List<LatLng>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromList(list: List<LatLng>): String {
        val type = object : TypeToken<List<LatLng>>() {}.type
        return Gson().toJson(list, type)
    }
}