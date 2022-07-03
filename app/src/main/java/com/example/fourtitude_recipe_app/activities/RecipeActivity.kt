package com.example.fourtitude_recipe_app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fourtitude_recipe_app.R
import com.example.fourtitude_recipe_app.adapter.RecipeAdapter
import com.example.fourtitude_recipe_app.adapter.RecipeClickListener
import com.example.fourtitude_recipe_app.database.DatabaseHandler
import com.example.fourtitude_recipe_app.databinding.ActivityMainBinding
import com.example.fourtitude_recipe_app.databinding.ActivityRecipeBinding
import com.example.fourtitude_recipe_app.model.RECIPE_ID_EXTRA
import com.example.fourtitude_recipe_app.model.RecipeModelClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.recipe_card.*
import kotlinx.android.synthetic.main.recipetypes.*

class RecipeActivity : AppCompatActivity(), RecipeClickListener {

    private lateinit var binding : ActivityRecipeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // Back button for App Bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Recipe List"

        // Putting Value in Spinner
        val spinner : Spinner = findViewById(R.id.recipeType)
        ArrayAdapter.createFromResource(
            this,
            R.array.arrayTypes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Getting Shared Preferences Value
        val sharedPref = this.getSharedPreferences("ADMIN_PREFS", Context.MODE_PRIVATE) ?: return
        val username = sharedPref.getString("username", null)

        // Only allowing admin to add recipes
        if(username == null) {
            floatBtnAdd.visibility = View.GONE
            floatBtnLogout.visibility = View.GONE
        }

        val logoutButton = floatBtnLogout
        logoutButton.setOnClickListener{
            var editor = sharedPref.edit()
            editor.clear()
            editor.remove("username")
            editor.commit()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Opening Add Recipe Page by Button
        val buttonClick = floatBtnAdd
        buttonClick.setOnClickListener{
            val intent = Intent(this, AddRecipe::class.java)
            startActivity(intent)
        }

        // Listing all recipe onto the recycle view
        setupListOfDataIntoRecycleView()

    }

    private fun setupListOfDataIntoRecycleView() {
        if(getRecipeList().size > 0) {
            rvRecipe.visibility = View.VISIBLE
            tvNoRecipe.visibility = View.GONE

            rvRecipe.layoutManager = GridLayoutManager(this, 2)
            val recipeAdapter = RecipeAdapter(this, getRecipeList(), this)

            rvRecipe.adapter = recipeAdapter
        } else {
            rvRecipe.visibility = View.GONE
            tvNoRecipe.visibility = View.VISIBLE
        }
    }


    private fun getRecipeList() : ArrayList<RecipeModelClass> {
        val databaseHandler : DatabaseHandler = DatabaseHandler(this)
        val recipeList : ArrayList<RecipeModelClass> = databaseHandler.viewRecipe()

        return recipeList
    }

    override fun onClick(recipe: RecipeModelClass) {
        val intent = Intent(applicationContext, RecipeDetails::class.java)
        intent.putExtra(RECIPE_ID_EXTRA, recipe.id)
        startActivity(intent)
    }
}