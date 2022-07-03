package com.example.fourtitude_recipe_app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.fourtitude_recipe_app.R
import com.example.fourtitude_recipe_app.database.DatabaseHandler
import com.example.fourtitude_recipe_app.databinding.ActivityRecipeDetailsBinding
import com.example.fourtitude_recipe_app.model.RECIPE_ID_EXTRA
import com.example.fourtitude_recipe_app.model.RecipeModelClass
import kotlinx.android.synthetic.main.activity_edit_recipe.*
import kotlinx.android.synthetic.main.activity_edit_recipe.btnUpdate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_details.*

class RecipeDetails : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button for App Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Adding Card View On Click by Binding
        val recipeID = intent.getIntExtra(RECIPE_ID_EXTRA, -1)
        val recipe = recipeFromID(recipeID)
        supportActionBar?.title = "${recipe?.name}"

        if (recipe != null) {
            binding.tvRecipeName.text = recipe.name
            binding.tvRecipeType.text = recipe.type
            binding.tvRecipeIngredients.text = recipe.ingredients
            binding.tvRecipeInstruction.text = recipe.instruction

        }


        // Getting Shared Preferences Value
        val sharedPref = this.getSharedPreferences("ADMIN_PREFS", Context.MODE_PRIVATE) ?: return
        val username = sharedPref.getString("username", null)

        // Only allowing admin to add recipes
        if(username == null) {
            btnUpdate.visibility = View.GONE
            btnDelete.visibility = View.GONE
        }

        // If Button Update is Pressed
        val buttonClick = btnUpdate
        buttonClick.setOnClickListener{
            val intent = Intent(this, EditRecipe::class.java)
            intent.putExtra(RECIPE_ID_EXTRA, recipeID)
            startActivity(intent)
        }

        // If Button Delete is Pressed
        binding.btnDelete.setOnClickListener {
            if (recipe != null) {
                deleteRecipe(recipe)
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

    private fun deleteRecipe(recipe: RecipeModelClass) {
        // Setting alert box for confirmation
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Recipe")
        builder.setMessage("Are you sure you want to delete this recipe")

        builder.setPositiveButton("Confirm") { dialogInterface, which ->
            val databaseHandler : DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteRecipe(RecipeModelClass(recipe.id, "", "", "", ""))
            if (status > -1) {
                val intent = Intent(this, RecipeActivity::class.java)
                startActivity(intent)
            }
        }

        builder.setNegativeButton("Cancel") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        // Creating the alert dialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()
    }
}