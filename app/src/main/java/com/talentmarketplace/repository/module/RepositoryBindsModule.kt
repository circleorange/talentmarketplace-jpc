package com.talentmarketplace.repository.module

import com.talentmarketplace.repository.JobPostRepository
import com.talentmarketplace.repository.auth.BasicAuthRepository
import com.talentmarketplace.repository.auth.GoogleAuthRepository
import com.talentmarketplace.repository.auth.basic.BasicAuthRepositoryImpl
import com.talentmarketplace.repository.auth.firebase.GoogleAuthRepositoryImpl
import com.talentmarketplace.repository.firestore.JobPostFirestoreRepository
import com.talentmarketplace.repository.mem.JobPostMemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Hilt module to provide dependencies
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindsModule {

    // Bind repository interface with implementation when using @Inject
    @Binds
    @Singleton
    abstract fun bindJobPostRepository(
        jobPostFirestoreRepository: JobPostFirestoreRepository
    ): JobPostRepository

    // Basic Authentication
    @Binds
    @Singleton
    abstract fun bindBasicAuthRepository(
        basicAuthManagerImpl: BasicAuthRepositoryImpl
    ): BasicAuthRepository

    @Binds
    @Singleton
    abstract fun bindGoogleAuthRepository(
        googleAuthRepositoryImpl: GoogleAuthRepositoryImpl
    ): GoogleAuthRepository
}
