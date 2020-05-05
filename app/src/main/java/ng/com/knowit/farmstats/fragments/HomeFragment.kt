package ng.com.knowit.farmstats.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.FragmentHomeBinding
import ng.com.knowit.farmstats.dialogs.NewFarmDialog
import ng.com.knowit.farmstats.dialogs.NewFarmerCustomDialog
import ng.com.knowit.farmstats.viewmodel.FarmViewModel
import ng.com.knowit.farmstats.viewmodel.FarmerViewModel


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var farmersViewModel: FarmerViewModel

    private lateinit var farmViewModel: FarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        farmersViewModel = ViewModelProviders.of(this).get(FarmerViewModel::class.java)

        farmViewModel = ViewModelProviders.of(this).get(FarmViewModel::class.java)

        binding.createFarmerCard.setOnClickListener {

            NewFarmerCustomDialog.display(childFragmentManager)
            //open dialog for farmer creation
        }

        binding.createFarmCard.setOnClickListener {
            //open dialog for farm creation
            NewFarmDialog.display(childFragmentManager)
        }

        loadPieChart(numberOfFarms(), numberOfFarmers())


    }

    private fun loadPieChart(numberOfFarms: Int, numberOfFarmers: Int) {

        if (numberOfFarms == 0 && numberOfFarmers == 0) {
            binding.noDataImageView.visibility = View.VISIBLE
        } else {

            binding.pieChart.visibility = View.VISIBLE

            val list = ArrayList<PieEntry>()

            list.add(PieEntry(numberOfFarmers.toFloat(), "Farmers"))
            list.add(PieEntry(numberOfFarms.toFloat(), "Farms"))
            val dataSet = PieDataSet(list, "Legend")

            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f
            dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

            val data = PieData(dataSet)
            binding.pieChart.data = data

            binding.pieChart.highlightValues(null)
            binding.pieChart.invalidate()
            binding.pieChart.description.text = "Farm-Farmers Chart"
        }


    }

    private fun numberOfFarmers() = farmersViewModel.getAllFarmersList().size

    private fun numberOfFarms() = farmViewModel.getAllFarmsList().size

}
