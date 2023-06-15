package eu.mobile.application.collector.fragment.positionModify

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentPositionModifyBinding
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.SubtitleMessage
import java.io.ByteArrayOutputStream
import java.time.LocalTime
import java.util.logging.Logger

@AndroidEntryPoint
class PositionModifyFragment : Fragment()  {

    companion object {
        fun newInstance() = PositionModifyFragment()
        val logger = Logger.getLogger(PositionModifyFragment::class.simpleName)

    }
    var imageUri: Uri? = null
    private val PICTURE_RESULT = 1
    val args: PositionModifyFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentPositionModifyBinding
    private val viewModel by viewModels<PositionModifyFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_position_modify, container, false)
        viewBinding = FragmentPositionModifyBinding.bind(root).apply {
            viewModel = this@PositionModifyFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = args.position
        viewModel.initialize(position)
        setupObservers()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = position.name })
    }
    private fun setupObservers() {
        viewModel.addedPositionLiveData.observe(viewLifecycleOwner){
            if(it){
                goToPositionDetails()
            }
        }
        viewModel.cameraClickedLiveData.observe(viewLifecycleOwner){
            if(it){
                openCamera()
            }
        }

        viewModel.positionImgNotifier.observe(viewLifecycleOwner){
            viewBinding.fragmentPositionModifyImage.setImageBitmap(it)
        }
    }

    private fun goToPositionDetails() {
        val navController = findNavController()
        //navController.previousBackStackEntry?.savedStateHandle?.set("position", viewModel.positionNotifier.value)
        navController.popBackStack()
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri!!)
        startActivityForResult(intent, PICTURE_RESULT)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        when (requestCode) {
            PICTURE_RESULT -> if (requestCode === PICTURE_RESULT) if (resultCode === Activity.RESULT_OK) {
                try {
                    val thumbnail = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, imageUri
                    )
                    val rotate = RotateBitmap(thumbnail, (90).toFloat()) ?: return
                    val compressedBitMap = compressBitmap(rotate)
                    viewBinding.fragmentPositionModifyImage.setImageBitmap(compressedBitMap)
                    viewModel.positionImgNotifier.value = compressedBitMap
                    viewModel.positionImgPathNotifier.value = requireContext().dataDir.absolutePath + '/' + LocalTime.now()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {

        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)

        val byteArray = stream.toByteArray()

        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
    fun RotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}