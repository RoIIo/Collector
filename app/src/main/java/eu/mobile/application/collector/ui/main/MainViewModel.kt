package eu.mobile.application.collector.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.repository.CategoryRepository
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val categoryRepository: CategoryRepository): ViewModel() {
    private val _liveData: MutableLiveData<Int> = MutableLiveData()
    val liveData: LiveData<Int> = _liveData

    fun onClick(){
        ErrorHandler.postMessageEvent(Message().apply { message = "Nacisnieto" })
    }

    fun onClickLoad() {
        viewModelScope.launch {
            categoryRepository.getCategories().onSuccess {
                ErrorHandler.postMessageEvent(Message().apply { message = it.firstOrNull()?.name ?: "BRAK" })
            }
                .onFailure {
                    ErrorHandler.postErrorMessageEvent(it)
                }
        }
    }
    fun onClickCategory() {
        viewModelScope.launch {
            categoryRepository.addCategory("TESTCATEGORY")
        }

    }
    fun initialize(){
    }
}