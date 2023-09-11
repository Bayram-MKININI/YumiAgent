package net.noliaware.yumi_agent.feature_auth.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import net.noliaware.yumi_agent.feature_auth.data.repository.AuthRepositoryImpl
import net.noliaware.yumi_agent.feature_auth.domain.repository.AuthRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository
}