package eu.mobile.application.collector.fragment.categoryList

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
class CategoryListViewModel @Inject constructor(val categoryRepository: CategoryRepository): ViewModel() {
    companion object {
        val logger = Logger.getLogger(CategoryListViewModel::class.simpleName)
    }
    var categoryEntryPressedNotifier = MutableLiveData<Boolean>()
    val categoryEntryPressed: LiveData<Boolean> = categoryEntryPressedNotifier

    var isLoaded = MutableLiveData(false)
    val categoryArray = MutableLiveData(arrayListOf<Category>())
    fun addCategoryPressed(){
        categoryEntryPressedNotifier.value = true
    }
    fun deleteCategory(position: Int){
        viewModelScope.launch {
            isLoaded.value = false

            val id = categoryArray.value?.get(position)?.Id ?: return@launch
            categoryRepository.deleteCategory(id)
                .onSuccess {
                    ErrorHandler.postMessageEvent(Message().apply { message = "Pomyślnie usunięto kategorię" })
                    categoryArray.value?.removeAt(position)
                }
                .onFailure {
                    ErrorHandler.postErrorMessageEvent(it)
                }
                    isLoaded.value = true
        }
    }

    fun initialize(){
        categoryEntryPressedNotifier.value = false
        loadCategories()
    }

    private fun loadCategories(){
        viewModelScope.launch {
            isLoaded.value = false
            categoryRepository.getCategories().onSuccess {
                logger.log(Level.INFO,"get categories: $it")
                categoryArray.value = ArrayList(it)
            }
                .onFailure {
                    logger.log(Level.WARNING, "Failed to get categories: $it")
                }
            isLoaded.value = true
        }
    }
}