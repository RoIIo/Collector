package eu.mobile.application.collector.fragment.categoryEntry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.event.ErrorHandler
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

    fun addCategory(){
        if(categoryEntryNotifier.value.isNullOrEmpty()){
            ErrorHandler.postMessageEvent(Message().apply { message = "Musisz wpisać nazwę kategorii" })
            return
        }
        viewModelScope.launch {
            repository.addCategory(categoryEntryNotifier.value!!)
                .onSuccess {
                    ErrorHandler.postMessageEvent(Message().apply {message = "Dodano kategorię"})
                    addedCategoryNotifier.value = true

                }
                .onFailure {
                    ErrorHandler.postErrorMessageEvent(it)
                }
        }

    }

    fun initialize(){

    }
}