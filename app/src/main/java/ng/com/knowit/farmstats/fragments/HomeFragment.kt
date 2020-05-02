package ng.com.knowit.farmstats.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.FragmentHomeBinding
import ng.com.knowit.farmstats.dialogs.NewFarmDialog
import ng.com.knowit.farmstats.dialogs.NewFarmerCustomDialog

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.createFarmerCard.setOnClickListener {

            NewFarmerCustomDialog.display(childFragmentManager)
            //open dialog for farmer creation
        }

        binding.createFarmCard.setOnClickListener {
            //open dialog for farm creation

            NewFarmDialog.display(childFragmentManager)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}
