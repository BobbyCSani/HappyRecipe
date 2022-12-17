package com.happy.recipe.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.happy.recipe.network.local.AppDatabase
import com.happy.recipe.network.local.FavoriteDao
import com.happy.recipe.network.local.RecipeDetailDao
import com.happy.recipe.network.local.RoomDatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RoomDatabaseModule::class]
)
class FakeDatabaseModule {

    @Provides
    @Singleton
    fun provideContext(): Context = ApplicationProvider.getApplicationContext()

    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()


    @Provides
    @Singleton
    fun provideRecipeDao(db: AppDatabase): RecipeDetailDao = db.recipeDetailDao()
}