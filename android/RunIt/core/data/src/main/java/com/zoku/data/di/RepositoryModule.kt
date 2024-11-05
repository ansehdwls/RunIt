package com.zoku.data.di

import com.zoku.data.repository.DataStoreRepository
import com.zoku.data.repository.DataStoreRepositoryImpl
import com.zoku.data.repository.LoginRepository
import com.zoku.data.repository.LoginRepositoryImpl
import com.zoku.data.repository.RunningRepository
import com.zoku.data.repository.RunningRepositoryImpl
import com.zoku.data.repository.TestRepository
import com.zoku.data.repository.TestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository

    @Singleton
    @Binds
    fun bindRunningRepository(
        runningRepositoryImpl: RunningRepositoryImpl
    ): RunningRepository

    @Singleton
    @Binds
    fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ) : LoginRepository

    @Binds
    @Singleton
    fun bindDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository
}