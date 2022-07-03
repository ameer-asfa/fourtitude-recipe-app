package com.example.fourtitude_recipe_app.activities


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import com.example.fourtitude_recipe_app.R
import com.example.fourtitude_recipe_app.database.DatabaseHandler
import com.example.fourtitude_recipe_app.model.RecipeModelClass
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class AddRecipe : AppCompatActivity() {
    var dbSq : DatabaseHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Back button for App Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Recipe"

        val addBtn = btnAdd
        btnAdd.setOnClickListener{
            addRecipe()
        }

    }

    private fun addRecipe() {
        val name = etName.text.toString()
        val type = etType.text.toString()
        val ingredient = etIngredients.text.toString()
        val instruction = etInstructions.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        if (!name.isEmpty() && !type.isEmpty() && !ingredient.isEmpty() && !instruction.isEmpty()) {
            val status = databaseHandler.addRecipe(RecipeModelClass(0, name, type, ingredient, instruction))

            if (status > -1) {
                val intent = Intent(this, RecipeActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Successfully added Recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
