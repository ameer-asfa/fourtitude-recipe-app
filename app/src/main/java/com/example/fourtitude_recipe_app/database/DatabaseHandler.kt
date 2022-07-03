package com.example.fourtitude_recipe_app.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fourtitude_recipe_app.model.RecipeModelClass

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "RecipeDatabase"
        private const val TABLE_RECIPE = "RecipeTable"
        private const val TABLE_ADMIN = "AdminTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_TYPE = "type"
        private val KEY_INGREDIENTS = "ingredient"
        private val KEY_INSTRUCTION = "instruction"

        private val KEY_ADMIN_ID = "_id"
        private val KEY_ADMIN_USERNAME = "name"
        private val KEY_ADMIN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Creating table with fields
        val CREATE_RECIPE_TABLE = ("CREATE TABLE " + TABLE_RECIPE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_INGREDIENTS + " TEXT,"
                + KEY_INSTRUCTION + " TEXT)")

        db?.execSQL(CREATE_RECIPE_TABLE)

        val CREATE_ADMIN_TABLE = ("CREATE TABLE " + TABLE_ADMIN + "("
                + KEY_ADMIN_ID + " INTEGER PRIMARY KEY,"
                + KEY_ADMIN_USERNAME + " TEXT,"
                + KEY_ADMIN_PASSWORD + " TEXT)")
        db?.execSQL(CREATE_ADMIN_TABLE)

        // Setting default values for admin
        val contentValues = ContentValues()
        contentValues.put(KEY_ADMIN_USERNAME, "admin")
        contentValues.put(KEY_ADMIN_PASSWORD, "admin")
        db?.insert(TABLE_ADMIN, null, contentValues)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPE")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        onCreate(db)
    }

    // Adding Recipe to Database
    fun addRecipe(recipe: RecipeModelClass) : Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, recipe.name)
        contentValues.put(KEY_TYPE, recipe.type)
        contentValues.put(KEY_INGREDIENTS, recipe.ingredients)
        contentValues.put(KEY_INSTRUCTION, recipe.instruction)

        // Inserting recipe details using insert query
        val success = db.insert(TABLE_RECIPE, null, contentValues)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewRecipe() : ArrayList<RecipeModelClass> {
        val recipeList : ArrayList<RecipeModelClass> = ArrayList<RecipeModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_RECIPE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var name : String
        var type : String
        var ingredients : String
        var instruction : String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                type = cursor.getString(cursor.getColumnIndex(KEY_TYPE))
                ingredients = cursor.getString(cursor.getColumnIndex(KEY_INGREDIENTS))
                instruction = cursor.getString(cursor.getColumnIndex(KEY_INSTRUCTION))

                val recipes = RecipeModelClass(id = id, name = name, type = type, ingredients = ingredients, instruction = instruction)
                recipeList.add(recipes)
            } while(cursor.moveToNext())
        }
        return recipeList
    }

    fun updateRecipe(recipe: RecipeModelClass) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, recipe.name)
        contentValues.put(KEY_TYPE, recipe.type)
        contentValues.put(KEY_INGREDIENTS, recipe.ingredients)
        contentValues.put(KEY_INSTRUCTION, recipe.instruction)

        val success = db.update(TABLE_RECIPE, contentValues, KEY_ID + "=" + recipe.id, null)

        db.close()
        return success
    }

    fun deleteRecipe(recipe: RecipeModelClass) : Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, recipe.id)

        val success = db.delete(TABLE_RECIPE, KEY_ID + "=" + recipe.id, null)

        db.close()
        return success
    }

    // Login Feature for Admin
    fun login(email: String, password: String) : Boolean {
        val columns = arrayOf(KEY_ADMIN_ID)
        val db = this.readableDatabase

        // Selection criteria
        val selection = "$KEY_ADMIN_USERNAME = ? AND $KEY_ADMIN_PASSWORD = ?"

        // Selection Arguments
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(TABLE_ADMIN, columns, selection, selectionArgs, null, null, null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if(cursorCount > 0)
            return true
        return false
    }

}