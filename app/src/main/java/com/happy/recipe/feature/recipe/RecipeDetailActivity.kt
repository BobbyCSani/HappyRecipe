package com.happy.recipe.feature.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.happy.recipe.R
import com.happy.recipe.data.model.RecipeDetailModel
import com.happy.recipe.databinding.ActivityRecipeDetailBinding
import com.happy.recipe.feature.base.HomeActivity
import com.happy.recipe.feature.base.WebViewActivity
import com.happy.recipe.utility.setSpannedText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RecipeDetailActivity: AppCompatActivity() {

    companion object{
        private const val RECIPE_ID_KEY = "RECIPE_ID_KEY"
        fun start(context: Context, recipeId: Int){
            context.startActivity(Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra(RECIPE_ID_KEY, recipeId)
            })
        }
    }

    private val viewModel by viewModels<RecipeDetailViewModel>()
    private val recipeId by lazy {
        intent.data?.path?.split("-")?.last()?.toInt()
            ?:
        intent.getIntExtra(RECIPE_ID_KEY, 0)
    }
    private lateinit var binding: ActivityRecipeDetailBinding
    private lateinit var ingredientAdapter: IngredientAdapter
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupObserver()
        viewModel.getRecipeDetail(recipeId)
    }

    private fun setupAdapter(){
        ingredientAdapter = IngredientAdapter()
        binding.rvIngredient.apply {
            layoutManager = LinearLayoutManager(this@RecipeDetailActivity)
            adapter = ingredientAdapter
        }
    }

    private fun setupObserver(){
        viewModel.recipeDetailLiveData.observe(this){ data ->
            binding.swipeRefresh.isRefreshing = false
            setupView(data)
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) startActivity(Intent(this, HomeActivity::class.java))
        else super.onBackPressed()
    }

    private fun setListener(data: RecipeDetailModel) = with(binding){
        swipeRefresh.setOnRefreshListener {
            viewModel.getRecipeDetail(recipeId)
        }

        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        btnShare.setOnClickListener {
            val text = buildString {
                append(data.title)
                append("\n")
                append(data.spoonacularSourceUrl)
            }
            createChooser(text)
        }

        btnFavorite.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                isFavorite = if (isFavorite) {
                    viewModel.deleteFavorite(data)
                    false
                } else {
                    viewModel.favorite(data)
                    true
                }
                setFavoriteButton()
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.isFavorite(data.id).collect{ result ->
                isFavorite = result
                setFavoriteButton()
            }
        }
    }

    private fun setupView(data: RecipeDetailModel) = with(binding){
        setListener(data)
        Glide.with(this@RecipeDetailActivity).load(data.image).into(ivCover)
        title.text = data.title
        summary.text = HtmlCompat.fromHtml(data.summary, HtmlCompat.FROM_HTML_MODE_COMPACT)
        person.text = getString(R.string.person_count, data.servings)
        servingTime.text = getString(R.string.cooking_time, data.readyInMinutes)
        creditText.setSpannedText(
            mainText = R.string.credit_text,
            clickableText = data.creditsText
        ){
            WebViewActivity.start(this@RecipeDetailActivity, data.sourceUrl)
        }
        detailInstruction.setSpannedText(
            mainText = R.string.instruction_detail_please_visit,
            clickableText = data.creditsText
        ){
            WebViewActivity.start(this@RecipeDetailActivity, data.sourceUrl)
        }
        ingredientAdapter.submitData(data.extendedIngredients)
    }

    private suspend fun setFavoriteButton() {
        withContext(Dispatchers.Main) {
            binding.btnFavorite.setImageDrawable(
                if (isFavorite) ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.ic_love_fill)
                else ContextCompat.getDrawable(this@RecipeDetailActivity, R.drawable.ic_love)
            )
        }
    }

    private fun createChooser(text: String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}