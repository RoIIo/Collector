package eu.mobile.application.collector.fragment.positionEntry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.repository.PositionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PositionEntryViewModel @Inject constructor(
    val positionRepository: PositionRepository
): ViewModel() {

    var positionEntryNotifier: MutableLiveData<String> = MutableLiveData()
    private val addedPositionNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val addedPositionLiveData: LiveData<Boolean> = addedPositionNotifier
    var category: Category? = null

    fun addPosition(){
        if(positionEntryNotifier.value.isNullOrEmpty()){
            EventBusHandler.postMessage(Message().apply { message = "Musisz wpisać nazwę pozycji" })
            return
        }
        viewModelScope.launch {
            positionRepository.addPosition(Position().apply { name = positionEntryNotifier.value!!
            categoryId = category?.Id})
                .onSuccess {
                    EventBusHandler.postMessage(Message().apply {message = "Dodano pozycję"})
                    addedPositionNotifier.value = true

                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                }
        }
    }
    fun initialize(category: Category){
        this.category = category
    }
}