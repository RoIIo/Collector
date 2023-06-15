package eu.mobile.application.kolekcjoner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.entity.Position
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class DBHelper @Inject constructor(
    @ApplicationContext private val context: Context
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "Collector.db"
        private val DATABASE_VERSION = 7

        val CATEGORIES_TABLE = "categories"
        val CATEGORY_ID = "id"
        val CATEGORY_NAME = "name"

        val POSITION_TABLE = "positions"
        val POSITION_ID = "id"
        val POSITION_NAME = "name"
        val POSITION_CATEGORY_ID = "category_id"
        val POSITION_IMAGE = "image"
        val POSITION_DESCRIPTION = "description"
        val POSITION_TOTAL = "total"
        val POSITION_ADD_DATE = "add_date"
        val POSITION_UPDATE_DATE = "update_date"
        val POSITION_PRODUCENT = "producent"
        val POSITION_PRICE = "price"
        val POSITION_CONDITION = "condition"
        val POSITION_SERIAL = "serial"
        val POSITION_ORIGIN = "origin"
        val POSITION_NOTES = "notes"

    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(tableCategoriesQuery)
        db.execSQL(tablePositionsQuery)
    }
    private val tableCategoriesQuery = "CREATE TABLE IF NOT EXISTS " + CATEGORIES_TABLE + " (" +
            CATEGORY_ID + " INTEGER PRIMARY KEY, " +
            CATEGORY_NAME + " TEXT)"

    private val tablePositionsQuery = "CREATE TABLE IF NOT EXISTS " + POSITION_TABLE + " (" +
            POSITION_ID + " INTEGER PRIMARY KEY, " +
            POSITION_NAME + " TEXT, " +
            POSITION_CATEGORY_ID + " INTEGER, " +
            POSITION_IMAGE + " TEXT, " +
            POSITION_DESCRIPTION + " TEXT, " +
            POSITION_TOTAL + " INTEGER, " +
            POSITION_ADD_DATE + " Text, " +
            POSITION_UPDATE_DATE + " Text, " +
            POSITION_PRODUCENT + " Text, " +
            POSITION_PRICE + " INTEGER, " +
            POSITION_CONDITION + " TEXT, " +
            POSITION_SERIAL + " Text, " +
            POSITION_ORIGIN + " Text, " +
            POSITION_NOTES + " Text, " +
            "FOREIGN KEY($POSITION_CATEGORY_ID) REFERENCES $CATEGORIES_TABLE($CATEGORY_ID))"

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $CATEGORIES_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $POSITION_TABLE")
        onCreate(db)
    }

    fun addCategory(category: Category): Category{
        val values = ContentValues()
        values.put(CATEGORY_NAME, category.name)
        val db = this.writableDatabase
        val id = db.insert(CATEGORIES_TABLE, null, values)
        db.close()
        category.Id = id.toInt()
        return category
    }
    fun getCategories(): List<Category>{
        val query = "SELECT * FROM $CATEGORIES_TABLE"
        val db = this.readableDatabase

        val cursor = db.rawQuery(query, null)

        var categories = ArrayList<Category>()
        while(cursor?.moveToNext() == true)
        {
            var category: Category?
            val id = cursor.getIntOrNull(0)
            val name = cursor.getStringOrNull(1)
            category = Category().apply {
                this.Id = id
                this.name = name}
            categories.add(category)
        }
        db.close()
        return categories
    }
    fun deleteCategory(id: Int) : Boolean{
        var result = false
        val query = "SELECT * from $CATEGORIES_TABLE WHERE $CATEGORY_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst())
        {
            val id_obj = cursor.getInt(0)
            db.delete(CATEGORIES_TABLE, "$CATEGORY_ID = ?", arrayOf(id_obj.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
    fun getPositions(categoryId: Int) : List<Position> {
        val query = "SELECT * FROM $POSITION_TABLE WHERE $POSITION_CATEGORY_ID=?;"
        var args: Array<String> = arrayOf("$categoryId")
        val db = this.readableDatabase

        val cursor = db.rawQuery(query, args)

        var positions = ArrayList<Position>()
        while(cursor?.moveToNext() == true)
        {
            var position: Position?
            val id = cursor.getIntOrNull(0)
            val name = cursor.getStringOrNull(1)
            val category = cursor.getIntOrNull(2)
            val image = cursor.getStringOrNull(3)
            val description = cursor.getStringOrNull(4)
            val total = cursor.getIntOrNull(5)
            val addDate = cursor.getStringOrNull(6)
            val updateDate = cursor.getStringOrNull(7)
            val producent = cursor.getStringOrNull(8)
            val price = cursor.getIntOrNull(9)
            val condition = cursor.getStringOrNull(10)
            val serial = cursor.getStringOrNull(11)
            val origin = cursor.getStringOrNull(12)
            val notes = cursor.getStringOrNull(13)
            position = Position().apply {
                this.Id = id
                this.name = name
                this.categoryId = category
                this.imagePath = image
                this.description = description
                this.total = total
                this.addDate = addDate
                this.updateDate = updateDate
                this.producent = producent
                this.price = price
                this.condition = condition
                this.serial = serial
                this.origin = origin
                this.notes = notes}
            positions.add(position)
        }
        db.close()
        return positions
    }
    fun getPosition(positionId: Int) : Position? {
        val query = "SELECT * FROM $POSITION_TABLE WHERE $POSITION_ID=?;"
        var args: Array<String> = arrayOf("$positionId")
        val db = this.readableDatabase

        val cursor = db.rawQuery(query, args)

        var position: Position? = null
        if(cursor.moveToFirst())
        {
            val id = cursor.getIntOrNull(0)
            val name = cursor.getStringOrNull(1)
            val category = cursor.getIntOrNull(2)
            val image = cursor.getStringOrNull(3)
            val description = cursor.getStringOrNull(4)
            val total = cursor.getIntOrNull(5)
            val addDate = cursor.getStringOrNull(6)
            val updateDate = cursor.getStringOrNull(7)
            val producent = cursor.getStringOrNull(8)
            val price = cursor.getIntOrNull(9)
            val condition = cursor.getStringOrNull(10)
            val serial = cursor.getStringOrNull(11)
            val origin = cursor.getStringOrNull(12)
            val notes = cursor.getStringOrNull(13)
            position = Position().apply {
                this.Id = id
                this.name = name
                this.categoryId = category
                this.imagePath = image
                this.description = description
                this.total = total
                this.addDate = addDate
                this.updateDate = updateDate
                this.producent = producent
                this.price = price
                this.condition = condition
                this.serial = serial
                this.origin = origin
                this.notes = notes}
        }
        db.close()
        return position
    }
    fun addPosition(position: Position): Position{
        val values = ContentValues()
        values.put(POSITION_NAME, position.name)
        values.put(POSITION_CATEGORY_ID, position.categoryId)
        values.put(POSITION_IMAGE, position.imagePath)
        values.put(POSITION_DESCRIPTION, position.description)
        values.put(POSITION_TOTAL, position.total)
        values.put(POSITION_ADD_DATE, position.addDate)
        values.put(POSITION_UPDATE_DATE, position.updateDate)
        values.put(POSITION_PRODUCENT, position.producent)
        values.put(POSITION_PRICE, position.price)
        values.put(POSITION_CONDITION, position.condition)
        values.put(POSITION_SERIAL,position.serial)
        values.put(POSITION_ORIGIN,position.origin)
        values.put(POSITION_NOTES, position.notes)
        val db = this.writableDatabase
        val id = db.insert(POSITION_TABLE, null, values)
        db.close()
        position.Id = id.toInt()
        return position
    }

    fun modifyPosition(position: Position): Position{
        val values = ContentValues()
        values.put(POSITION_NAME, position.name)
        values.put(POSITION_CATEGORY_ID, position.categoryId)
        values.put(POSITION_IMAGE, position.imagePath)
        values.put(POSITION_DESCRIPTION, position.description)
        values.put(POSITION_TOTAL, position.total)
        values.put(POSITION_ADD_DATE, position.addDate)
        values.put(POSITION_UPDATE_DATE, position.updateDate)
        values.put(POSITION_PRODUCENT, position.producent)
        values.put(POSITION_PRICE, position.price)
        values.put(POSITION_CONDITION, position.condition)
        values.put(POSITION_SERIAL,position.serial)
        values.put(POSITION_ORIGIN,position.origin)
        values.put(POSITION_NOTES, position.notes)
        val db = this.writableDatabase
        db.update(POSITION_TABLE, values,"$POSITION_ID = ?", arrayOf(position.Id.toString()))
        db.close()
        return position
    }
    fun deletePosition(id: Int) : Boolean{
        var result = false
        val query = "SELECT * from $POSITION_TABLE WHERE $POSITION_ID = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        if(cursor.moveToFirst())
        {
            val id_obj = cursor.getInt(0)
            db.delete(POSITION_TABLE, "$POSITION_ID = ?", arrayOf(id_obj.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
}