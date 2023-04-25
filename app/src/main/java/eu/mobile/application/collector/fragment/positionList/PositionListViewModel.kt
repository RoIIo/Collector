package eu.mobile.application.collector.fragment.positionList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PositionListViewModel @Inject constructor(): ViewModel() {


    private val positionPressedNotifier = MutableLiveData<Boolean>()
    val positionPressed: LiveData<Boolean> = positionPressedNotifier

    fun addPositionPressed(){
        positionPressedNotifier.value = true
    }
    fun initialize(){
        positionPressedNotifier.value = false
    }
}