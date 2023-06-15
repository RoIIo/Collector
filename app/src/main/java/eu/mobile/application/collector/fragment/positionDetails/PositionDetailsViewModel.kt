package eu.mobile.application.collector.fragment.positionDetails

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Position
import javax.inject.Inject

@HiltViewModel
class PositionDetailsViewModel @Inject constructor(): ViewModel() {

    var isLoadingNotifier = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingNotifier

    val modifyPositionNotifier: MutableLiveData<Boolean> = MutableLiveData(false)
    val modifyPositionLiveData: LiveData<Boolean> = modifyPositionNotifier

    var positionNameNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionNameVisible: LiveData<Boolean> = positionNameNotifier.map { !it.isNullOrEmpty() }

    var positionImageNotifier: MutableLiveData<Bitmap?> = MutableLiveData()
    val isPositionImageVisible: LiveData<Boolean> = positionImageNotifier.map { it != null }

    var positionTotalNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionTotalVisible: LiveData<Boolean> = positionTotalNotifier.map { !it.isNullOrEmpty() }

    var positionDescriptionNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionDescriptionVisible: LiveData<Boolean> = positionDescriptionNotifier.map { !it.isNullOrEmpty() }

    var positionNotesNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionNotesVisible: LiveData<Boolean> = positionNotesNotifier.map { !it.isNullOrEmpty() }

    var positionProducentNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionProducentVisible: LiveData<Boolean> = positionProducentNotifier.map { !it.isNullOrEmpty() }

    var positionPriceNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionPriceVisible: LiveData<Boolean> = positionPriceNotifier.map { !it.isNullOrEmpty() }

    var positionConditionNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionConditionVisible: LiveData<Boolean> = positionConditionNotifier.map { !it.isNullOrEmpty() }

    var positionSerialNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionSerialVisible: LiveData<Boolean> = positionSerialNotifier.map { !it.isNullOrEmpty() }

    var positionOriginNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionOriginVisible: LiveData<Boolean> = positionOriginNotifier.map { !it.isNullOrEmpty() }

    var positionAddDateNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionAddDateVisible: LiveData<Boolean> = positionAddDateNotifier.map { !it.isNullOrEmpty() }

    var positionUpdateDateNotifier: MutableLiveData<String> = MutableLiveData()
    val isPositionUpdateDateVisible: LiveData<Boolean> = positionAddDateNotifier.map { !it.isNullOrEmpty() }
    fun initialize(position : Position){
        positionNameNotifier.value = position.name
        positionTotalNotifier.value = position.total?.toString()
        positionDescriptionNotifier.value = position.description
        positionNotesNotifier.value = position.notes
        positionProducentNotifier.value = position.producent
        positionPriceNotifier.value = position.price.toString()
        positionConditionNotifier.value = position.condition
        positionSerialNotifier.value = position.serial
        positionOriginNotifier.value = position.origin
        positionAddDateNotifier.value = position.addDate
        positionUpdateDateNotifier.value = position.updateDate
        modifyPositionNotifier.value = false
        loadImage(position.imagePath)
    }

    fun modifyPositionPressed(){
        modifyPositionNotifier.value = true
    }
    private fun loadImage(path: String?) {
        if(path.isNullOrEmpty()) return

        val bitmap = BitmapFactory.decodeFile(path)
        positionImageNotifier.value = bitmap
    }

}