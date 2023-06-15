package eu.mobile.application.collector.fragment.positionModify

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.database.getIntOrNull
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
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
    var positionNotesNotifier: MutableLiveData<String> = MutableLiveData()
    var positionProducentNotifier: MutableLiveData<String> = MutableLiveData()
    var positionPriceNotifier: MutableLiveData<String> = MutableLiveData()
    var positionConditionNotifier: MutableLiveData<String> = MutableLiveData()
    var positionSerialNotifier: MutableLiveData<String> = MutableLiveData()
    var positionOriginNotifier: MutableLiveData<String> = MutableLiveData()
    var positionAddDateNotifier: MutableLiveData<String> = MutableLiveData()

    var positionImgNotifier: MutableLiveData<Bitmap> = MutableLiveData()
    var positionImgPathNotifier: MutableLiveData<String> = MutableLiveData()

    private val addedPositionNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val addedPositionLiveData: LiveData<Boolean> = addedPositionNotifier
    val cameraClickedNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val cameraClickedLiveData: LiveData<Boolean> = cameraClickedNotifier
    var categoryId: Int? = null
    fun acceptPositionPressed(){
        if (positionNameNotifier.value.isNullOrEmpty()) {
            EventBusHandler.postMessage(Message().apply { message = "Musisz wpisać nazwę pozycji" })
            return
        }
        val oldPath = positionNotifier.value?.imagePath
        viewModelScope.launch {
            isLoadingNotifier.value = true
            positionRepository.modifyPosition(positionNotifier.value!!.apply {
                name = positionNameNotifier.value!!
                categoryId = categoryId
                imageBitMap = positionImgNotifier.value
                imagePath = positionImgPathNotifier.value
                description = positionDescriptionNotifier.value
                total = positionTotalNotifier.value?.toIntOrNull()
                producent = positionProducentNotifier.value
                price = positionPriceNotifier.value?.toIntOrNull()
                condition = positionConditionNotifier.value
                serial = positionSerialNotifier.value
                origin = positionOriginNotifier.value
                notes = positionNotesNotifier.value
                addDate = positionAddDateNotifier.value
                updateDate =  SimpleDateFormat("dd-MM-yyyy", Locale("pl", "PL")).format(Date())
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
        categoryId = position.categoryId
        positionTotalNotifier.value = position.total?.toString()
        positionDescriptionNotifier.value = position.description
        positionNotesNotifier.value = position.notes
        positionProducentNotifier.value = position.producent
        positionPriceNotifier.value = position.price?.toString()
        positionConditionNotifier.value = position.condition
        positionSerialNotifier.value = position.serial
        positionOriginNotifier.value = position.origin
        positionAddDateNotifier.value = position.addDate
        positionImgPathNotifier.value = position.imagePath
        loadImage(position.imagePath)
    }

    private fun loadImage(path: String?) {
        if(path.isNullOrEmpty()) return

        val bitmap = BitmapFactory.decodeFile(path)
        positionImgNotifier.value = bitmap
    }
}