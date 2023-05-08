package eu.mobile.application.collector.fragment.initialization

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import eu.mobile.application.collector.MainActivity
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentCategoryListBinding
import eu.mobile.application.collector.databinding.FragmentInitializationBinding
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.SubtitleMessage
import eu.mobile.application.collector.fragment.categoryList.CategoryListFragment
import eu.mobile.application.collector.fragment.categoryList.CategoryListViewModel
import java.util.Dictionary
import java.util.logging.Logger
import kotlin.system.exitProcess

class InitializationFragment: Fragment() {
    companion object {
        fun newInstance() = InitializationFragment()
        val logger = Logger.getLogger(InitializationFragment::class.simpleName)
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    private lateinit var layout: View
    private lateinit var viewBinding: FragmentInitializationBinding
    private val viewModel by viewModels<InitializationFragmentViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_initialization, container, false)
        viewBinding = FragmentInitializationBinding.bind(root).apply {
            viewModel = this@InitializationFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        layout = viewBinding.mainLayout

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = "" })
        viewModel.initialize()
        if (hasPermissions(activity as Context, PERMISSIONS)) {
            goToCategoryList()
        }
    }

    private fun setupObservers() {
        viewModel.permissionLiveData.observe(viewLifecycleOwner){
            if(it)
                checkPermissionRequired()
        }
    }
    private fun goToCategoryList(){
        findNavController().navigate(R.id.action_initializationFragment_to_categoryListFragment)
    }

    private fun checkPermissionRequired() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // direct navigate to respective screen

        }
        activity?.let {
            if (hasPermissions(activity as Context, PERMISSIONS)) {
                goToCategoryList()
            } else {
                    permReqLauncher.launch(
                    PERMISSIONS
                )
            }
        }
    }
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                goToCategoryList()
            }else{
                // show custom alert
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                showPermissionDialog()
            }
        }
    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.fragment_initialization_permission_required)
        builder.setMessage(R.string.fragment_initialization_permission_message)
        builder.setPositiveButton(R.string.fragment_initialization_permission_ok) { dialog, which ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton(R.string.fragment_initialization_permission_cancel) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}