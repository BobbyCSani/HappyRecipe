package com.happy.recipe

import com.happy.recipe.data.model.Ingredient
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.model.RecipeModel
import org.junit.Test

import org.junit.Assert.*

fun generateRecipeDetail()=
    RecipeDetailModel(
        id = 12345,
        title = "Happy Fresh",
        image = "",
        summary = "happy and fresh",
        servings = 3,
        readyInMinutes = 15,
        extendedIngredients = arrayListOf(Ingredient(original = "garlic")),
        instructions = "cook until you bored",
        spoonacularSourceUrl = "",
        creditsText = "author",
        sourceUrl = ""
    )

fun generateRecipeModel() =
    RecipeModel(
        id = 12345,
        title = "Happy Fresh",
        image = "",
        summary = "happy and fresh",
        servings = 3,
        readyInMinutes = 15
    )