package eu.mobile.application.collector.fragment.initialization

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import eu.mobile.application.collector.R

class InitializationFragmentViewModel: ViewModel() {

    private val permissionNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val permissionLiveData: LiveData<Boolean> = permissionNotifier
    fun onClickRequestPermission() {
       permissionNotifier.value = true
    }
    fun initialize(){
        permissionNotifier.value = false
    }
}