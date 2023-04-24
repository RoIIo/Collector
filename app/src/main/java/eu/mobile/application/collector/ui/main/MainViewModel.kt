package eu.mobile.application.collector.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.repository.CategoryRepository
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.Logger

import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val categoryRepository: CategoryRepository): ViewModel() {
    companion object {
        val logger = Logger.getLogger(MainViewModel::class.simpleName)
    }
    var addCategoryPressed = MutableLiveData(false)
    var categoryPressed = MutableLiveData(-1)
    var isLoaded = MutableLiveData(false)
    var categoryArray: MutableLiveData<Array<Category>> = MutableLiveData(arrayOf())


    fun addCategoryPressed(){
        addCategoryPressed.value = true
    }
    fun categoryPressed(name: String){

    }
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
        loadCategories()
    }

    private fun loadCategories(){
        viewModelScope.launch {
            isLoaded.value = false
            categoryRepository.getCategories().onSuccess {
                logger.log(Level.INFO,"get categories: $it")
                categoryArray.value = it.toTypedArray()
            }
                .onFailure {
                    logger.log(Level.WARNING, "Failed to get categories: $it")
                }
            isLoaded.value = true
        }
    }
}