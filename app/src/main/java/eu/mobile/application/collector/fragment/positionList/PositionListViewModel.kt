package eu.mobile.application.collector.fragment.positionList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.fragment.categoryList.CategoryListFragment
import eu.mobile.application.collector.fragment.categoryList.CategoryListViewModel
import eu.mobile.application.collector.repository.PositionRepository
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class PositionListViewModel @Inject constructor(
    private val positionRepository: PositionRepository
): ViewModel() {
    companion object{
        var logger =  Logger.getLogger(PositionListViewModel::class.simpleName)
    }

    var isLoaded = MutableLiveData(false)
    var category: Category? = null
    private val positionPressedNotifier = MutableLiveData<Boolean>()
    val positionPressed: LiveData<Boolean> = positionPressedNotifier

    val positionArrayNotifier: MutableLiveData<ArrayList<Position>> = MutableLiveData(arrayListOf())
    fun addPositionPressed(){
        positionPressedNotifier.value = true
    }
    fun deletePosition(position: Int){
        viewModelScope.launch {
            isLoaded.value = false

            val id = positionArrayNotifier.value?.get(position)?.Id ?: return@launch
            positionRepository.deletePosition(id)
                .onSuccess {
                    logger.info("Pomyślnie usunięto pozycję")
                    EventBusHandler.postMessage(Message().apply { message = "Pomyślnie usunięto pozycję" })
                    positionArrayNotifier.value?.removeAt(position)
                    val newList = MutableLiveData(arrayListOf<Position>())
                    newList.value?.addAll(positionArrayNotifier.value!!)
                    positionArrayNotifier.value = newList.value
                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                    logger.warning("Nie udało się usunąć kategorii: $it")
                }
            isLoaded.value = true
        }
    }
    fun initialize(category: Category){
        positionPressedNotifier.value = false
        this.category = category
        loadPositions(category.Id!!)
    }

    private fun loadPositions(categoryId: Int) {
        viewModelScope.launch {
            isLoaded.value = false
            positionRepository.getPositions(categoryId)
                .onSuccess {
                    positionArrayNotifier.value = ArrayList(it)
                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                }
            isLoaded.value = true
        }

    }
}