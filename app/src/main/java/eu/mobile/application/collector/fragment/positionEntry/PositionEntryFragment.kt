package eu.mobile.application.collector.fragment.positionEntry

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentPositionEntryBinding
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.SubtitleMessage
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.logging.Logger


@AndroidEntryPoint
class PositionEntryFragment : Fragment()  {

    companion object {
        fun newInstance() = PositionEntryFragment()
        val logger = Logger.getLogger(PositionEntryFragment::class.simpleName)

    }
    var vFilename: String = ""
    var imageUri: Uri? = null
    private val PICTURE_RESULT = 1
    private lateinit var viewBinding: FragmentPositionEntryBinding
    private val viewModel by viewModels<PositionEntryViewModel>()

    private val args: PositionEntryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_position_entry, container, false)
        viewBinding = FragmentPositionEntryBinding.bind(root).apply {
            viewModel = this@PositionEntryFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize(args.category)
        setupObservers()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = args.category.name })
    }

    private fun setupObservers() {
        viewModel.addedPositionLiveData.observe(viewLifecycleOwner){
            if(it){
                goToPositionList()
            }
        }
        viewModel.cameraButtonLiveData.observe(viewLifecycleOwner){
            if(it){
                openCamera()
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri!!)
        startActivityForResult(intent, PICTURE_RESULT)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        when (requestCode) {
            PICTURE_RESULT -> if (requestCode === PICTURE_RESULT) if (resultCode === RESULT_OK) {
                try {
                    val thumbnail = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, imageUri
                    )
                    val rotate = RotateBitmap(thumbnail, (90).toFloat())
                    viewBinding.imgViewer.setImageBitmap(rotate)
                    viewModel.positionImgNotifier.value = rotate
                    viewModel.positionImgPathNotifier.value = requireContext().dataDir.absolutePath + '/' + LocalTime.now()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun RotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
    /*fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }*/
    private fun goToPositionList() {
        if(!findNavController().popBackStack(R.id.positionListFragmentDestination,false))
            findNavController().navigate(R.id.positionListFragmentDestination)
    }
}