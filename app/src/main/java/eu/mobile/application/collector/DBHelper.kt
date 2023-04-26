package eu.mobile.application.kolekcjoner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.collector.event.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBHelper @Inject constructor(
    @ApplicationContext private val context: Context
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "Collector.db"
        private val DATABASE_VERSION = 1

        val Categiories_table = "categories"
        val Details_table = "details"
        val CATEGORY_NAME = "category"
        val ID_COL = "id"
        val NAME_COl = "name"
        val AGE_COL = "age"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(tableCategoriesQuery)
        db.execSQL(tableDetailsQuery)
    }
    private val tableCategoriesQuery = "CREATE TABLE IF NOT EXISTS " + Categiories_table + " (" +
            ID_COL + " INTEGER PRIMARY KEY, " +
            CATEGORY_NAME + " TEXT)"

    private val tableDetailsQuery = "CREATE TABLE IF NOT EXISTS " + Details_table + " (" +
            ID_COL + " INTEGER PRIMARY KEY)"

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $Categiories_table")
        db.execSQL("DROP TABLE IF EXISTS $Details_table")
        onCreate(db)
    }

    fun addCategory(category: String){
        val values = ContentValues()
        values.put(CATEGORY_NAME, category)
        val db = this.writableDatabase
        db.insert(Categiories_table, null, values)
        db.close()
    }
    fun getCategories(): List<Category>{
        val query = "SELECT * FROM $Categiories_table"
        val db = this.readableDatabase

        val cursor = db.rawQuery(query, null)

        var categories = ArrayList<Category>()
        while(cursor?.moveToNext() == true)
        {
            var category: Category?
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
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
        val query = "SELECT * from $Categiories_table WHERE $ID_COL = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst())
        {
            val id_obj = cursor.getInt(0)
            db.delete(Categiories_table, "$ID_COL = ?", arrayOf(id_obj.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
    fun getPositions(categoryId: Int) : List<Position> {
        ErrorHandler.postMessageEvent(Message().apply { message = "Not implemented categoryId = $categoryId" })
        return listOf()
    }
}