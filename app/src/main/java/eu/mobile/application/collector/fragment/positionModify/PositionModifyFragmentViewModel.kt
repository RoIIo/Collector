package eu.mobile.application.collector.fragment.positionModify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Position
import javax.inject.Inject

@HiltViewModel
class PositionModifyFragmentViewModel @Inject constructor(): ViewModel() {
    var isLoadingNotifier = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingNotifier

    fun acceptPositionPressed(){}

    fun initialize(position: Position){

    }
}