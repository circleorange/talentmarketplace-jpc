package com.talentmarketplace.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

// Hilt module to provide dependencies
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    // Bind repository interface with memory repository
    @Binds
    abstract fun bindJobPostingRepository(
        jobPostingMemRepository: JobPostingMemRepository
    ): JobPostingRepository
}
