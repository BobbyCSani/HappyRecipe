package com.happy.recipe

import com.happy.recipe.data.model.Ingredient
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.data.model.RecipeModel
import com.happy.recipe.network.local.AppDatabase
import com.happy.recipe.network.local.FavoriteDao
import com.happy.recipe.network.local.RecipeDetailDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class AppDatabaseTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject lateinit var favoriteDao: FavoriteDao
    @Inject lateinit var recipeDetailDao: RecipeDetailDao
    @Inject lateinit var db: AppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadFavorite(){
        val data = generateRecipeModel()
        favoriteDao.insertFavorite(listOf(data))
        val getData = favoriteDao.getAllFavoriteRecipes()
        assertEquals(data.id, getData[0].id)
    }

    @Test
    fun writeReadByIdAndDeleteFavorite(){
        val data = generateRecipeModel()
        favoriteDao.insertFavorite(listOf(data))
        val getData = favoriteDao.getAllFavoriteRecipes()
        assertEquals(data.id, getData[0].id)
        favoriteDao.deleteFavorite(getData[0])
        val getDeleted = favoriteDao.getFavorite(data.id)
        assert(getDeleted.isEmpty())
    }

    @Test
    fun writeAndReadRecipeDetail(){
        val data = generateRecipeDetail()
        recipeDetailDao.insertRecipe(listOf(data))
        val getData = recipeDetailDao.getDetailRecipe(data.id)
        assertEquals(data.id, getData[0].id)
    }

}