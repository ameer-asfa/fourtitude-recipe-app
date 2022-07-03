package com.example.fourtitude_recipe_app.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.fourtitude_recipe_app.databinding.ActivityRecipeDetailsBinding
import com.example.fourtitude_recipe_app.databinding.RecipeCardBinding
import com.example.fourtitude_recipe_app.model.RecipeModelClass

class RecipeViewHolder(private val cardCellBinding: RecipeCardBinding, private val clickListener: RecipeClickListener) : RecyclerView.ViewHolder(cardCellBinding.root) {
    fun bindRecipe(recipe: RecipeModelClass) {
        cardCellBinding.tvRecipeName.text = recipe.name
        cardCellBinding.tvRecipeType.text = recipe.type
//        cardCellBinding.tvRecipeIngredients.text = recipe.ingredients
//        cardCellBinding.tvRecipeInstruction.text = recipe.instruction

        cardCellBinding.cardView.setOnClickListener {
            clickListener.onClick(recipe)
        }

    }
}