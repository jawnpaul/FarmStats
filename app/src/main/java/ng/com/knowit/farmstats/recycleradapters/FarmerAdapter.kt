package ng.com.knowit.farmstats.recycleradapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.model.Farmer

class FarmerAdapter(val farmerList: List<Farmer>, val context: Context) :
    RecyclerView.Adapter<FarmerAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val farmerNameTextView =
            itemView.findViewById(R.id.single_farmer_name_text_view) as TextView
        val farmerAddressTextView =
            itemView.findViewById(R.id.single_farmer_address_text_view) as TextView

        val farmerPhoneTextView =
            itemView.findViewById(R.id.single_farmer_phone_text_view) as TextView

        val farmerImageView =
            itemView.findViewById(R.id.single_farmer_image_view) as CircleImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_farmer_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = farmerList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val farmer = farmerList[position]

        holder.farmerNameTextView.text = farmer.farmerFullName
        holder.farmerAddressTextView.text = farmer.farmerAddress
        holder.farmerPhoneTextView.text = farmer.farmerPhoneNumber
        Glide.with(context)
            .load(Uri.parse(farmer.farmerPhotoUri))
            .placeholder(R.drawable.undraw_male_avatar)
            .into(holder.farmerImageView)

    }
}