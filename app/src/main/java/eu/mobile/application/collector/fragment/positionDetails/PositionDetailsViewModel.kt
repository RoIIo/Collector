package eu.mobile.application.collector.fragment.positionDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Position
import javax.inject.Inject

@HiltViewModel
class PositionDetailsViewModel @Inject constructor(): ViewModel() {
val positionNameNotifier: MutableLiveData<String> = MutableLiveData()
    fun initialize(position : Position){
        positionNameNotifier.value = position.name
    }
}