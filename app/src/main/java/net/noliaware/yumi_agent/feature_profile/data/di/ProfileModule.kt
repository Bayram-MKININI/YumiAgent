package net.noliaware.yumi_agent.feature_profile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import net.noliaware.yumi_agent.feature_profile.data.repository.ProfileRepositoryImpl
import net.noliaware.yumi_agent.feature_profile.domain.repository.ProfileRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {
    @Binds
    @ViewModelScoped
    abstract fun bindProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}