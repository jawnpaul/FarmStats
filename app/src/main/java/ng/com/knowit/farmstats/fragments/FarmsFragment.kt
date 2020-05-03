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
import ng.com.knowit.farmstats.databinding.FragmentFarmsBinding
import ng.com.knowit.farmstats.recycleradapters.FarmAdapter
import ng.com.knowit.farmstats.ui.FarmViewModel

/**
 * A simple [Fragment] subclass.
 */
class FarmsFragment : Fragment() {

    private lateinit var binding: FragmentFarmsBinding

    private lateinit var farmViewModel: FarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_farms, container, false)

        farmViewModel = ViewModelProviders.of(this).get(FarmViewModel::class.java)

        binding.farmRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        farmViewModel.getAllFarms().observe(this, Observer { farms ->

            val adapter = FarmAdapter(farms)

            binding.farmRecyclerView.adapter = adapter

        })

        super.onViewCreated(view, savedInstanceState)
    }
}
