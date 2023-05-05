package eu.mobile.application.collector.repository

import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
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
                EventBusHandler.postErrorMessage(ex)
                Result.failure(ex)
            }
        }
    }
    suspend fun deletePosition(positionId: Int): Result<Boolean>{
        return withContext(Dispatchers.IO) {
            try {
                val result = db.deletePosition(positionId)
                Result.success(result)
            }
            catch (ex: java.lang.Exception){
                CategoryRepository.logger.warning("Wystąpił błąd podczas usuwania Pozycji Id: $positionId. $ex")
                EventBusHandler.postErrorMessage(ex)
                Result.failure(ex)
            }
        }
    }
}