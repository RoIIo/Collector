package eu.mobile.application.collector.repository

import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.kolekcjoner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.logging.Logger
import javax.inject.Inject

class CategoryRepository @Inject constructor(val db: DBHelper) {
    companion object {
        val logger = Logger.getLogger(CategoryRepository::class.simpleName)
    }
        suspend fun getCategories(): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.getCategories()
                Result.success(result)
            }
            catch (ex: Exception) {
                logger.warning("Wystąpił błąd podczas pobierania kategorii. $ex")
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
                logger.warning("Wystąpił błąd podczas dodawania kategorii: $category. $ex")
                ErrorHandler.postErrorMessageEvent(ex)
                Result.failure(ex)
            }
        }
    }

    suspend fun deleteCategory(id: Int): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.deleteCategory(id)
                Result.success(result)
            }
            catch (ex: java.lang.Exception){
                logger.warning("Wystąpił błąd podczas usuwania kategorii Id: $id. $ex")
                ErrorHandler.postErrorMessageEvent(ex)
                Result.failure(ex)
            }
        }
    }
}