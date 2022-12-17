package com.happy.recipe

import androidx.paging.PagingSource
import com.happy.recipe.data.repository.RecipeRepoImpl
import com.happy.recipe.data.repository.RecipeRepository
import com.happy.recipe.network.local.AppDatabase
import com.happy.recipe.network.remote.ApiService
import com.happy.recipe.utility.pagination.BasePagingSource
import com.happy.recipe.utility.pagination.PagingDataModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class RecipeRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val apiService = mockk<ApiService>()
    @Inject lateinit var db: AppDatabase
    private lateinit var repository: RecipeRepository

    @Before
    fun setup() {
        hiltRule.inject()
        repository = RecipeRepoImpl(apiService, db)
    }

    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun getRecipeDetailFromNetwork(){
        coEvery {
            apiService.getRecipeDetail(1234)
        } coAnswers {
            Response.success(generateRecipeDetail())
        }
        runBlocking {
            val response = repository.getRecipeDetail(1234)
            response.collectLatest { data ->
                assertEquals(data?.id, generateRecipeDetail().id)
            }
        }
    }

    @Test
    fun getRecipeDetailFromLocal(){
        db.recipeDetailDao().insertRecipe(listOf(generateRecipeDetail()))

        coEvery {
            apiService.getRecipeDetail(12345)
        } coAnswers {
            throw Exception()
        }

        runBlocking {
            val response = repository.getRecipeDetail(12345)
            response.collectLatest { data ->
                assertEquals(data?.id, generateRecipeDetail().id)
            }
        }
    }

    @Test
    fun getSearchSuggestion(){
        coEvery {
            apiService.getAutocompleteSearch(query = "happy")
        } coAnswers {
            Response.success(listOf(generateRecipeModel()))
        }
        runBlocking {
            val response = repository.getSearchSuggestion("happy")
            response.collectLatest { data ->
                assertEquals(data, listOf(generateRecipeDetail().title))
            }
        }
    }

    @Test
    fun testPagingData() = runBlocking {
        val expectedResult = listOf(generateRecipeModel())

        val pagingSource = BasePagingSource {
            PagingDataModel(
                responseCode = 1,
                data = expectedResult,
                pageCount = 1,
                isSuccess = true
            )
        }

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(generateRecipeModel()),
            prevKey = null,
            nextKey = null,
        )
        assertEquals(expected, actual)
    }
}