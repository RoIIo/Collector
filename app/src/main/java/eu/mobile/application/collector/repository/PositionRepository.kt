package eu.mobile.application.collector.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.kolekcjoner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Logger
import javax.inject.Inject


class PositionRepository  @Inject constructor(val db: DBHelper) {
    companion object {
        val logger = Logger.getLogger(PositionRepository::class.simpleName)
    }
    suspend fun getPositions(categoryId: Int): Result<List<Position>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.getPositions(categoryId)
                logger.info("SUCESSFULLY GET POSITION")
                Result.success(result)
            }
            catch (ex: Exception) {
                logger.info("FAILED TO GET POSITION")

                EventBusHandler.postErrorMessage(ex)
                Result.failure(ex)
            }
        }
    }

    suspend fun addPosition(position: Position): Result<Position>{
        return withContext(Dispatchers.IO) {
            try {
                val result  = db.addPosition(position)
                saveImage(position.imageBitMap, position.imagePath)
                logger.info("SUCESSFULLY ADD POSITION")
                Result.success(result)
            }
            catch (ex: Exception) {
                logger.warning("FAILED TO ADD POSITION $ex")
                EventBusHandler.postErrorMessage(ex)
                Result.failure(ex)
        }
        }
    }

    suspend fun deletePosition(position: Position): Result<Boolean>{
        return withContext(Dispatchers.IO) {
            try {
                val result = db.deletePosition(position.Id!!)
                deleteImage(position.imagePath)
                Result.success(result)
            }
            catch (ex: java.lang.Exception){
                logger.warning("Wystąpił błąd podczas usuwania Pozycji Id: ${position.Id}. $ex")
                EventBusHandler.postErrorMessage(ex)
                Result.failure(ex)
            }
        }
    }

    suspend fun modifyPosition(position: Position): Result<Position>{
        return withContext(Dispatchers.IO) {
            try {
                val result  = db.modifyPosition(position)
                saveImage(position.imageBitMap, position.imagePath)
                logger.info("SUCESSFULLY MODIFIED POSITION")
                Result.success(result)
            }
            catch (ex: Exception) {
                logger.warning("FAILED TO MODIFY POSITION $ex")
                EventBusHandler.postErrorMessage(ex)
                Result.failure(ex)
            }
        }
    }

    private fun deleteImage(imagePath: String?) {
        val file = File(imagePath)
        file.delete()
    }
    private fun saveImage(imageBitMap: Bitmap?,filePath: String?) {
        if(imageBitMap!= null && !filePath.isNullOrEmpty()){
            val file = File(filePath)
            val fOut = FileOutputStream(file)

            imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
        }
    }
}