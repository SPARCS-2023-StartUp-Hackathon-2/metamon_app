package com.andes.metamon.core.di

import com.andes.metamon.auth.data.repository.LoginRepositoryImpl
import com.andes.metamon.auth.domain.repository.LoginRepository
import com.andes.metamon.core.data.repository.CertificateRepositoryImpl
import com.andes.metamon.core.domain.repository.CertificateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsLoginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    fun bindsImageRepository(repository: CertificateRepositoryImpl): CertificateRepository
}
