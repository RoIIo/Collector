package eu.mobile.application.collector.repository

import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.kolekcjoner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PositionRepository  @Inject constructor(val db: DBHelper) {
    suspend fun getPositions(categoryId: Int): Result<List<Position>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.getPositions(categoryId)
                Result.success(result)
            }
            catch (ex: Exception) {
                ErrorHandler.postErrorMessageEvent(ex)
                Result.failure(ex)
            }
        }
    }
}