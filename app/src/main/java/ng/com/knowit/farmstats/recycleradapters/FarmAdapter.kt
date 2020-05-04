package ng.com.knowit.farmstats.recycleradapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.model.Farm

class FarmAdapter(private val farmList: List<Farm>) :
    RecyclerView.Adapter<FarmAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val farmNameTextView = itemView.findViewById(R.id.single_farm_name_text_view) as TextView
        val farmLocationTextView =
            itemView.findViewById(R.id.single_farm_location_text_view) as TextView

        val farmOwnerTextView = itemView.findViewById(R.id.single_farm_owner_text_view) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_farm_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = farmList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val farm = farmList[position]

        holder.farmNameTextView.text = farm.farmName
        holder.farmLocationTextView.text = farm.farmLocation
        holder.farmOwnerTextView.text = farm.farmerName


    }
}