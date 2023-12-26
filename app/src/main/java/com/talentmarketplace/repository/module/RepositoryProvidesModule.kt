package com.talentmarketplace.repository.module

import android.content.Context
import android.content.SharedPreferences
import com.talentmarketplace.repository.firestore.UserFirestoreRepository
import com.talentmarketplace.utils.SignInMethodManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryProvidesModule {
    // Provides used to tell dagger how to provide instances that cannot be constructor-injected
    @Provides
    @Singleton
    fun provideFirestoreUserRepository(): UserFirestoreRepository = UserFirestoreRepository()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideSignInMethodManager(sharedPreferences: SharedPreferences): SignInMethodManager {
        return SignInMethodManager(sharedPreferences)
    }
}