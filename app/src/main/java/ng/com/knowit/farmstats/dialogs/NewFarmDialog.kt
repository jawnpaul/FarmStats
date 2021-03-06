package ng.com.knowit.farmstats.dialogs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.textfield.TextInputLayout
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.NewFarmDialogLayoutBinding
import ng.com.knowit.farmstats.db.FarmDatabase
import ng.com.knowit.farmstats.db.FarmerDao
import ng.com.knowit.farmstats.db.FarmerDatabase
import ng.com.knowit.farmstats.model.Farm
import ng.com.knowit.farmstats.model.Farmer
import ng.com.knowit.farmstats.utility.Utils


class NewFarmDialog : DialogFragment(), OnMapReadyCallback {

    private lateinit var binding: NewFarmDialogLayoutBinding

    private lateinit var mMap: GoogleMap

    private lateinit var marker: Marker

    private lateinit var spinner: Spinner

    private var farmersArray = arrayOf<String>()

    private val REQUEST_LOCATION_PERMISSION = 1

    val farmCoordinates = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.new_farm_dialog_layout, container, false)

        spinner = binding.farmersSpinner


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rootView = view.findViewById<View>(R.id.new_farmer_linear_layout)
        val textInputLayouts: List<TextInputLayout> = Utils.findViewsWithType(
            rootView, TextInputLayout::class.java
        )
        binding.newFarmerToolbar.inflateMenu(R.menu.new_farm_menu)
        binding.newFarmerToolbar.setTitleTextColor(resources.getColor(R.color.colorText))
        binding.newFarmerToolbar.title = "Create new Farm"
        binding.newFarmerToolbar.setNavigationOnClickListener { dismiss() }
        binding.newFarmerToolbar.setOnMenuItemClickListener {
            var noErrors = true
            for (textInputLayout in textInputLayouts) {
                val editTextString =
                    textInputLayout.editText!!.text.toString()
                if (editTextString.isEmpty()) {
                    textInputLayout.error = resources.getString(R.string.error_string)
                    noErrors = false
                } else {
                    textInputLayout.error = null
                }
            }
            if (noErrors) {
                //save farm
                val farmName = binding.farmNameInputEditText.text.toString()
                val farmLocation = binding.farmLocationInputEditText.text.toString()

                val farmerFullName = spinner.selectedItem.toString()

                saveFarm(farmerFullName, farmName, farmLocation, farmCoordinates)
                /*if (farmerFullName.isNotEmpty()) {

                    saveFarm(farmerFullName, farmName, farmLocation, farmCoordinates)
                    //dismiss()
                } else {
                    Toast.makeText(context!!, "No farmers created", Toast.LENGTH_SHORT).show()

                }*/
                //dismiss()
            }
            true
        }

        binding.saveFarmButton.setOnClickListener {

            var noErrors = true
            for (textInputLayout in textInputLayouts) {
                val editTextString =
                    textInputLayout.editText!!.text.toString()
                if (editTextString.isEmpty()) {
                    textInputLayout.error = resources.getString(R.string.error_string)
                    noErrors = false
                } else {
                    textInputLayout.error = null
                }
            }
            if (noErrors) {
                //save farm
                val farmName = binding.farmNameInputEditText.text.toString()
                val farmLocation = binding.farmLocationInputEditText.text.toString()

                val farmerFullName = spinner.selectedItem.toString()

                saveFarm(farmerFullName, farmName, farmLocation, farmCoordinates)

            }
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.farm_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        farmersArray = farmersNames(context!!)


        val farmerNamesAdapter =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, farmersArray)
        farmerNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = farmerNamesAdapter

    }

    companion object {
        private val TAG = NewFarmDialog::class.java.simpleName

        fun display(fragmentManager: FragmentManager): NewFarmDialog {
            val newFarmDialog = NewFarmDialog()
            newFarmDialog.show(fragmentManager, TAG)
            return newFarmDialog
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setOnMapClick(mMap)

        //setPoiClick(mMap)

        enableLocation()

        mMap.setOnMarkerClickListener { marker ->
            marker.remove()
            farmCoordinates.remove(marker.position)

            updateMap(farmCoordinates)
            true
        }


    }

    private fun setOnMapClick(map: GoogleMap) {
        map.setOnMapClickListener { latLng ->
            placeMarkerOnMap(latLng)
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        marker = mMap.addMarker(markerOptions)

        farmCoordinates.add(location)
        updateMap(farmCoordinates)

    }



    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableLocation()
            }
        }
    }

    private fun getPolygonOptions(list: List<LatLng>): PolygonOptions {
        val pathOptions = PolygonOptions().strokeColor(Color.BLACK)
        pathOptions.addAll(list)
        return pathOptions
    }

    private fun updateMap(list: List<LatLng>) {

        if (list.size >= 1) {
            mMap.addPolygon(getPolygonOptions(list))
        } else {
            mMap.clear()
        }

    }

    private fun farmersNames(context: Context): Array<String> {
        val farmersArrayList: ArrayList<Farmer>

        val farmerDao: FarmerDao =
            FarmerDatabase.DatabaseProvider.getFarmerDatabase(context).farmerDao()

        farmersArrayList = farmerDao.getAllFarmerList() as ArrayList<Farmer>

        val farmersNamesList = ArrayList<String>()
        farmersNamesList.add("Select a Farmer")
        for (farmer in farmersArrayList) {
            farmersNamesList.add(farmer.farmerFullName!!)
        }
        val array = arrayOfNulls<String>(farmersNamesList.size)

        return farmersNamesList.toArray(array)
    }

    private fun getFarmerLocalId(context: Context, farmerFullName: String): Int {
        val farmerDao: FarmerDao =
            FarmerDatabase.DatabaseProvider.getFarmerDatabase(context).farmerDao()
        return farmerDao.getFarmerByFullName(farmerFullName).farmerLocalId
    }

    private fun saveFarm(
        farmerName: String,
        farmName: String,
        farmLocation: String,
        farmCoordinates: List<LatLng>
    ) {
        val farmDao = FarmDatabase.DatabaseProvider.getFarmDatabase(context!!).farmDao()

        if (farmerName == "Select a Farmer") {

            Toast.makeText(context!!, "Choose a valid Farmer", Toast.LENGTH_SHORT)
                .show()

        } else {

            //Checks for valid coordinates
            if (farmCoordinates.size >= 1) {
                farmDao.insert(
                    Farm(
                        farmerName,
                        getFarmerLocalId(context!!, farmerName),
                        farmName, farmLocation, farmCoordinates
                    )
                )
                Log.d("FarmDialog", "Farm Saved")
                dismiss()
            } else {
                Toast.makeText(context!!, "Select farm boundaries on the map", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }
}