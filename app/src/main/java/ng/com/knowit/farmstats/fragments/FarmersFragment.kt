package ng.com.knowit.farmstats.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.FragmentFarmersBinding
import ng.com.knowit.farmstats.dialogs.NewFarmerCustomDialog
import ng.com.knowit.farmstats.recycleradapters.FarmerAdapter
import ng.com.knowit.farmstats.ui.FarmerViewModel

/**
 * A simple [Fragment] subclass.
 */
class FarmersFragment : Fragment() {

    private lateinit var binding: FragmentFarmersBinding

    private lateinit var farmerViewModel: FarmerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_farmers, container, false)

        binding.farmersRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        farmerViewModel = ViewModelProviders.of(this).get(FarmerViewModel::class.java)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        farmerViewModel.getAllFarmers().observe(this, Observer { farmers ->

            if (farmers.isEmpty()) {
                binding.addFarmerImageView.visibility = View.VISIBLE
            } else {
                binding.addFarmerImageView.visibility = View.GONE
            }

            val adapter = FarmerAdapter(farmers, context!!)

            binding.farmersRecyclerView.adapter = adapter


        })


        binding.addFarmerImageView.setOnClickListener {

            NewFarmerCustomDialog.display(childFragmentManager)
            //open dialog for farmer creation
        }

        super.onViewCreated(view, savedInstanceState)
    }
}
