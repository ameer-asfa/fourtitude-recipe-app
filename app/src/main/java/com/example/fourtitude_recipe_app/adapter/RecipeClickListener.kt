package com.example.fourtitude_recipe_app.adapter

import com.example.fourtitude_recipe_app.model.RecipeModelClass

interface RecipeClickListener {
    fun onClick(recipe: RecipeModelClass)
}