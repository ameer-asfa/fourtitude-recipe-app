package com.example.fourtitude_recipe_app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.fourtitude_recipe_app.R
import com.example.fourtitude_recipe_app.database.DatabaseHandler
import com.example.fourtitude_recipe_app.databinding.ActivityEditRecipeBinding
import com.example.fourtitude_recipe_app.databinding.ActivityRecipeDetailsBinding
import com.example.fourtitude_recipe_app.model.RECIPE_ID_EXTRA
import com.example.fourtitude_recipe_app.model.RecipeModelClass

class EditRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button for App Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Recipe"

        val recipeID = intent.getIntExtra(RECIPE_ID_EXTRA, -1)
        val recipe = recipeFromID(recipeID)

        if (recipe != null) {
            binding.etName.setText(recipe.name)
            binding.etType.setText(recipe.type)
            binding.etIngredients.setText(recipe.ingredients)
            binding.etInstructions.setText(recipe.instruction)
        }

        binding.btnUpdate.setOnClickListener {
            if (recipe != null) {
                updateRecipe(recipe)
            }
        }
    }

    private fun recipeFromID(recipeID: Int) : RecipeModelClass? {
        val databaseHandler : DatabaseHandler = DatabaseHandler(this)
        val recipeList : ArrayList<RecipeModelClass> = databaseHandler.viewRecipe()

        for(recipe in recipeList) {
            if (recipeID == recipe.id) {
                return recipe
            }
        }
        return null
    }

    private fun updateRecipe(recipe: RecipeModelClass) {
        val name = binding.etName.text.toString()
        val type = binding.etType.text.toString()
        val ingredients = binding.etIngredients.text.toString()
        val instruction = binding.etInstructions.text.toString()

        val databaseHandler : DatabaseHandler = DatabaseHandler(this)

        val status = databaseHandler.updateRecipe(RecipeModelClass(recipe.id, name, type, ingredients, instruction))
        if (status > -1) {
            val intent = Intent(this, RecipeDetails::class.java)
            intent.putExtra(RECIPE_ID_EXTRA, recipe.id)
            startActivity(intent)
        }
    }

}