package com.example.fourtitude_recipe_app.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fourtitude_recipe_app.R
import com.example.fourtitude_recipe_app.adapter.RecipeViewHolder
import com.example.fourtitude_recipe_app.model.RecipeModelClass
import com.example.fourtitude_recipe_app.databinding.RecipeCardBinding
import kotlinx.android.synthetic.main.recipe_card.view.*

class RecipeAdapter(val context: Context, val recipes: ArrayList<RecipeModelClass>, private val clickListener: RecipeClickListener) : RecyclerView.Adapter<RecipeViewHolder>() {

//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val tvRecipeName = view.tvRecipeName
//        val tvRecipeType = view.tvRecipeType
//        val cardView = view.cardViewMain
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.recipe_card,
//                parent,
//                false
//            )
//        )

        val from = LayoutInflater.from(parent.context)
        val binding = RecipeCardBinding.inflate(from, parent, false)
        return RecipeViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bindRecipe(recipes[position])

    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}