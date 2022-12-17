package com.happy.recipe.feature.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RecipeDomainType: Parcelable {
    @Parcelize
    object Favorite: RecipeDomainType()

    @Parcelize
    class Search(val query: String): RecipeDomainType()

    @Parcelize
    object Home: RecipeDomainType()
}