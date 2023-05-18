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
    val isPositionDescriptionVisible: LiveData<Boolean> = positionNameNotifier.map { !it.isNullOrEmpty() }

    var positionRatingNotifier: MutableLiveData<Int?> = MutableLiveData()
    val isPositionRatingVisible: LiveData<Boolean> = positionRatingNotifier.map { it != null }
    fun initialize(position : Position){
        positionNameNotifier.value = position.name
        positionTotalNotifier.value = position.total?.toString()
        positionDescriptionNotifier.value = position.description
        positionRatingNotifier.value = position.rating
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