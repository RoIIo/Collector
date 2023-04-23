package eu.mobile.application.collector.repository

import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.kolekcjoner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class CategoryRepository @Inject constructor(val db: DBHelper) {
    suspend fun getCategories(): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.getCategories()
                Result.success(result)
            }
            catch (ex: Exception) {
                ErrorHandler.postErrorMessageEvent(ex)
                Result.failure(ex)
            }
        }
    }

    suspend fun addCategory(category: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.addCategory(category)
                Result.success(result)
            }
            catch (ex: java.lang.Exception){
                ErrorHandler.postErrorMessageEvent(ex)
                Result.failure(ex)
            }
        }
    }
}