package ng.com.knowit.farmstats.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import ng.com.knowit.farmstats.R
import ng.com.knowit.farmstats.databinding.NewFarmerDialogLayoutBinding
import ng.com.knowit.farmstats.db.FarmerDatabase
import ng.com.knowit.farmstats.model.Farmer
import ng.com.knowit.farmstats.utility.Utils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NewFarmerCustomDialog : DialogFragment() {

    private lateinit var binding: NewFarmerDialogLayoutBinding

    private val TAG: String = NewFarmerCustomDialog::class.java.simpleName

    private var photoUri: Uri? = null

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
            DataBindingUtil.inflate(inflater, R.layout.new_farmer_dialog_layout, container, false)
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
        binding.newFarmerToolbar.inflateMenu(R.menu.new_farmer_menu)
        binding.newFarmerToolbar.setTitleTextColor(resources.getColor(R.color.colorText))
        binding.newFarmerToolbar.title = "Create new Farmer"
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
                //save farmer
                val farmerFirstName = binding.farmerFirstNameInputEditText.text.toString()
                val farmerLastNme = binding.farmerLastNameInputEditText.text.toString()
                val farmerAddress = binding.farmerAddressInputEditText.text.toString()
                val farmerPhoneNumber = binding.farmerPhoneNumberInputEditText.text.toString()

                saveFarmer(
                    farmerFirstName,
                    farmerLastNme,
                    farmerAddress,
                    farmerPhoneNumber,
                    photoUri
                )
                dismiss()
            }
            true
        }

        binding.saveFarmerButton.setOnClickListener {

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
                //save farmer
                val farmerFirstName = binding.farmerFirstNameInputEditText.text.toString()
                val farmerLastNme = binding.farmerLastNameInputEditText.text.toString()
                val farmerAddress = binding.farmerAddressInputEditText.text.toString()
                val farmerPhoneNumber = binding.farmerPhoneNumberInputEditText.text.toString()

                saveFarmer(
                    farmerFirstName,
                    farmerLastNme,
                    farmerAddress,
                    farmerPhoneNumber,
                    photoUri
                )
                dismiss()
            }

        }

        binding.farmerImage.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }


    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(context!!, "ng.com.knowit.farmstats", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    Log.d(TAG, photoURI.toString())
                    photoUri = photoURI
                    Glide.with(context!!).load(photoURI)
                        .placeholder(R.drawable.undraw_male_avatar)
                        .into(binding.farmerImage)
                }
            }
        }
    }


    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun saveFarmer(
        farmerFirstName: String,
        farmerLastName: String,
        farmerAddress: String,
        farmerPhoneNumber: String,
        farmerPhotoUri: Uri?
    ) {
        val farmerDao = FarmerDatabase.DatabaseProvider.getFarmerDatabase(context!!).farmerDao()

        farmerDao.insert(
            Farmer(
                farmerFirstName,
                farmerLastName,
                farmerAddress,
                farmerPhoneNumber,
                farmerPhotoUri.toString(),
                farmerFirstName + " " + farmerLastName,
                System.currentTimeMillis().toString()
            )
        )
        Log.d(TAG, "Saved")
    }

    companion object {
        private val TAG = NewFarmerCustomDialog::class.java.simpleName

        fun display(fragmentManager: FragmentManager): NewFarmerCustomDialog {
            val newFarmerCustomDialog = NewFarmerCustomDialog()
            newFarmerCustomDialog.show(fragmentManager, TAG)
            return newFarmerCustomDialog
        }
    }

}