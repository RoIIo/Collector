package eu.mobile.application.collector.fragment.positionEntry

import android.graphics.Bitmap
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
import java.util.logging.Logger
import javax.inject.Inject
@HiltViewModel
class PositionEntryViewModel @Inject constructor(
    val positionRepository: PositionRepository
): ViewModel() {
    companion object {
        val logger = Logger.getLogger(PositionEntryViewModel::class.simpleName)

    }
    var isLoadingNotifier = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingNotifier

    var positionNameNotifier: MutableLiveData<String> = MutableLiveData()
    var positionDescriptionNotifier: MutableLiveData<String> = MutableLiveData()
    var positionTotalNotifier: MutableLiveData<String> = MutableLiveData()
    var positionRatingNotifier: MutableLiveData<Int> = MutableLiveData()
    var positionImgNotifier: MutableLiveData<Bitmap> = MutableLiveData()
    var positionImgPathNotifier: MutableLiveData<String> = MutableLiveData()

    private val addedPositionNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val addedPositionLiveData: LiveData<Boolean> = addedPositionNotifier
    val cameraClickedNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val cameraClickedLiveData: LiveData<Boolean> = cameraClickedNotifier
    var category: Category? = null

    fun imageClicked(){
        cameraClickedNotifier.value = true
    }


    fun addPosition() {
        if (positionNameNotifier.value.isNullOrEmpty()) {
            EventBusHandler.postMessage(Message().apply { message = "Musisz wpisać nazwę pozycji" })
            return
        }
        viewModelScope.launch {
            isLoadingNotifier.value = true
            positionRepository.addPosition(Position().apply {
                name = positionNameNotifier.value!!
                categoryId = category?.Id
                imageBitMap = positionImgNotifier.value
                imagePath = positionImgPathNotifier.value
                description = positionDescriptionNotifier.value
                total = positionTotalNotifier.value?.toIntOrNull()
                rating = positionRatingNotifier.value
            })
                .onSuccess {
                    EventBusHandler.postMessage(Message().apply { message = "Dodano pozycję" })
                    addedPositionNotifier.value = true
                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                }
            isLoadingNotifier.value = false
        }
    }

    fun initialize(category: Category){
        this.category = category
    }
}