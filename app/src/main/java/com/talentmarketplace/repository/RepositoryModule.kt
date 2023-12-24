package com.talentmarketplace.repository

import com.talentmarketplace.repository.auth.AuthRepository
import com.talentmarketplace.repository.auth.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Hilt module to provide dependencies
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Bind repository interface with memory repository
    @Binds
    @Singleton
    abstract fun bindJobPostingRepository(
        jobPostingMemRepository: JobPostingMemRepository
    ): JobPostingRepository

    // User Authentication
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
