package ng.com.knowit.farmstats.dialogs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.textfield.TextInputLayout
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.NewFarmDialogLayoutBinding
import ng.com.knowit.farmstats.utility.Utils


class NewFarmDialog : DialogFragment(), OnMapReadyCallback {

    private lateinit var binding: NewFarmDialogLayoutBinding

    private lateinit var mMap: GoogleMap

    private lateinit var marker: Marker

    private val REQUEST_LOCATION_PERMISSION = 1

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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
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
                val farmerFirstName = binding.farmNameInputEditText.text.toString()
                val farmerLastNme = binding.farmLocationInputEditText.text.toString()




                dismiss()
            }
            true
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.farm_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
        setMapLongClick(mMap)

        setPoiClick(mMap)

        enableLocation()

        mMap.setOnMarkerClickListener { marker ->
            marker.remove()
            true
        }
    }

    private fun setMapLongClick(map: GoogleMap) {


        map.setOnMapLongClickListener { latLng ->

            placeMarkerOnMap(latLng)
            /*val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(MarkerOptions().position(latLng)
                    //.title(getString(R.string.dropped_pin))
                .snippet(snippet)
            )
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F))
*/
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
        //val titleStr = getAddress(location)
        //markerOptions.title(titleStr)
        marker = mMap.addMarker(markerOptions)
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
}