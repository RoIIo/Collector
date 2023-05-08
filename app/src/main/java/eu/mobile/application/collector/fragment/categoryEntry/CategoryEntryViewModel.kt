package eu.mobile.application.collector.fragment.categoryEntry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.repository.CategoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryEntryViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {
    var categoryEntryNotifier: MutableLiveData<String> = MutableLiveData()
    private val addedCategoryNotifier: MutableLiveData<Boolean> = MutableLiveData()
    val addedCategoryLiveData: LiveData<Boolean> = addedCategoryNotifier

    var isLoadingNotifier = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingNotifier
    fun addCategory(){
        if(categoryEntryNotifier.value.isNullOrEmpty()){
            EventBusHandler.postMessage(Message().apply { message = "Musisz wpisać nazwę kategorii" })
            return
        }
        viewModelScope.launch {
            repository.addCategory(Category().apply { name = categoryEntryNotifier.value!!})
                .onSuccess {
                    EventBusHandler.postMessage(Message().apply {message = "Dodano kategorię"})
                    addedCategoryNotifier.value = true

                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                }
        }

    }

    fun initialize(){

    }
}