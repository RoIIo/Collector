package eu.mobile.application.collector.fragment.positionList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.repository.PositionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionListViewModel @Inject constructor(
    private val positionRepository: PositionRepository
): ViewModel() {

    var isLoaded = MutableLiveData(false)

    private val positionPressedNotifier = MutableLiveData<Boolean>()
    val positionPressed: LiveData<Boolean> = positionPressedNotifier

    val positionListNotifier = MutableLiveData<ArrayList<Position>>(arrayListOf())
    fun addPositionPressed(){
        positionPressedNotifier.value = true
    }
    fun initialize(categoryId: Int){
        positionPressedNotifier.value = false
        loadPositions(categoryId)
    }

    private fun loadPositions(categoryId: Int) {
        viewModelScope.launch {
            isLoaded.value = false
            positionRepository.getPositions(categoryId)
                .onSuccess {
                    positionListNotifier.value = ArrayList(it)
                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                }
            isLoaded.value = true
        }

    }
}