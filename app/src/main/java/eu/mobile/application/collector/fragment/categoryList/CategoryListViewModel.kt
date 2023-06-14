package eu.mobile.application.collector.fragment.categoryList

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

    var isLoadingNotifier = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoadingNotifier
    val categoryArrayNotifier: MutableLiveData<ArrayList<Category>> = MutableLiveData(arrayListOf())
    fun addCategoryPressed(){
        categoryEntryPressedNotifier.value = true
    }
    fun deleteCategory(position: Int){
        viewModelScope.launch {
            isLoadingNotifier.value = true

            val id = categoryArrayNotifier.value?.get(position)?.Id ?: return@launch
            categoryRepository.deleteCategory(id)
                .onSuccess {
                    logger.info("Pomyślnie usunięto kategorię")
                    EventBusHandler.postMessage(Message().apply { message = "Pomyślnie usunięto kategorię" })
                    categoryArrayNotifier.value?.removeAt(position)
                    val newList = MutableLiveData(arrayListOf<Category>())
                    newList.value?.addAll(categoryArrayNotifier.value!!)
                    categoryArrayNotifier.value = newList.value
                }
                .onFailure {
                    EventBusHandler.postErrorMessage(it)
                    logger.warning("Nie udało się usunąć kategorii: $it")
                }
            isLoadingNotifier.value = false
        }
    }

    fun initialize(){
        categoryEntryPressedNotifier.value = false
        loadCategories()
    }

    private fun loadCategories(){
        viewModelScope.launch {
            isLoadingNotifier.value = true
            categoryRepository.getCategories().onSuccess {
                logger.log(Level.INFO,"get categories: $it. Size: ${it.size}")
                categoryArrayNotifier.value = ArrayList(it)
            }
                .onFailure {
                    logger.log(Level.WARNING, "Failed to get categories: $it")
                }
            isLoadingNotifier.value = false
        }
    }
}