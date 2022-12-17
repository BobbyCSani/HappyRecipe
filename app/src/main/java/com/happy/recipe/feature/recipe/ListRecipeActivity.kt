package com.happy.recipe.feature.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.happy.recipe.R
import com.happy.recipe.databinding.ActivityListRecipeBinding
import com.happy.recipe.utility.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListRecipeActivity: AppCompatActivity() {

    companion object{
        fun start(context: Context, domainType: RecipeDomainType){
            context.startActivity(Intent(context, ListRecipeActivity::class.java).apply {
                putExtra(RecipeCardFragment.DOMAIN_TYPE, domainType)
            })
        }
    }

    private lateinit var binding: ActivityListRecipeBinding
    private val domainType by lazy { intent?.parcelable<RecipeDomainType>(RecipeCardFragment.DOMAIN_TYPE) ?: RecipeDomainType.Home }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView(){
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val fragment = RecipeCardFragment.newInstance(domainType)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, RecipeCardFragment.TAG)
            .commit()
    }
}