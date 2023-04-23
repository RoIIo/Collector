package eu.mobile.application.collector.repository

import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.entity.Details
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.kolekcjoner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRepository  @Inject constructor(val db: DBHelper) {
    suspend fun getDetails(categoryId: Int): Result<List<Details>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.getDetails(categoryId)
                Result.success(result)
            }
            catch (ex: Exception) {
                ErrorHandler.postErrorMessageEvent(ex)
                Result.failure(ex)
            }
        }
    }
}