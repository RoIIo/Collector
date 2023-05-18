package eu.mobile.application.collector.fragment.positionModify

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.repository.PositionRepository
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PositionModifyFragmentViewModel @Inject constructor(
    val positionRepository: PositionRepository
): ViewModel() {

    var isLoadingNotifier = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingNotifier

    var positionNotifier: MutableLiveData<Position> = MutableLiveData()

    var positionNameNotifier: MutableLiveData<String> = MutableLiveData()
    var positionDescriptionNotifier: MutableLiveData<String> = MutableLiveData()
    var positionTotalNotifier: MutableLiveData<String> = MutableLiveData()
    var positionRatingNotifier: MutableLiveData<Int> = MutableLiveData()
    var positionImageNotifier: MutableLiveData<Bitmap> = MutableLiveData()
    var positionImgPathNotifier: MutableLiveData<String> = MutableLiveData()

    private val addedPositionNotifier: MutableLiveData<Boolean> = MutableLiveData(false)
    val addedPositionLiveData: LiveData<Boolean> = addedPositionNotifier
    val cameraClickedNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val cameraClickedLiveData: LiveData<Boolean> = cameraClickedNotifier
    fun acceptPositionPressed(){
        if (positionNameNotifier.value.isNullOrEmpty()) {
            EventBusHandler.postMessage(Message().apply { message = "Musisz wpisać nazwę pozycji" })
            return
        }
        val oldPath = positionNotifier.value?.imagePath
        viewModelScope.launch {
            isLoadingNotifier.value = true
            positionRepository.modifyPosition(positionNotifier.value!!.apply {
                name = positionNameNotifier.value
                imageBitMap = positionImageNotifier.value
                imagePath = positionImgPathNotifier.value
                description = positionDescriptionNotifier.value
                total = positionTotalNotifier.value?.toIntOrNull()
                rating = positionRatingNotifier.value
            })
                .onSuccess {
                    EventBusHandler.postMessage(Message().apply { message = "Zmodyfikowano pozycję" })
                    if(oldPath != it.imagePath && !oldPath.isNullOrEmpty()) {
                        val file = File(oldPath)
                        file.delete()
                    }
                    addedPositionNotifier.value = true
                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                }
            isLoadingNotifier.value = false
        }

    }
    fun imageClicked(){
        cameraClickedNotifier.value = true
    }


    fun initialize(position: Position){
        positionNotifier.value = position
        positionNameNotifier.value = position.name
        positionTotalNotifier.value = position.total?.toString()
        positionDescriptionNotifier.value = position.description
        positionRatingNotifier.value = position.rating
        addedPositionNotifier.value = false
        loadImage(position.imagePath)
    }

    private fun loadImage(path: String?) {
        if(path.isNullOrEmpty()) return

        val bitmap = BitmapFactory.decodeFile(path)
        positionImageNotifier.value = bitmap
    }
}