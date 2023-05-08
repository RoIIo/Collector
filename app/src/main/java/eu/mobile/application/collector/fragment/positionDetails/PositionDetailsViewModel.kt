package eu.mobile.application.collector.fragment.positionDetails

import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Position
import javax.inject.Inject

@HiltViewModel
class PositionDetailsViewModel @Inject constructor(): ViewModel() {
    var positionNotifier: MutableLiveData<Position> = MutableLiveData()
val positionNameNotifier: MutableLiveData<String> = MutableLiveData()
    fun initialize(position : Position){
        positionNameNotifier.value = position.name
        positionNotifier.value = position
        loadImage(position)
    }

    private fun loadImage(position: Position) {
        val bitmap = BitmapFactory.decodeFile(position.imagePath)
        position.imageBitMap = bitmap
    }

}