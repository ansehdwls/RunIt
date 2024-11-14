package com.zoku.data.di

import com.zoku.data.repository.AttendanceRepository
import com.zoku.data.repository.AttendanceRepositoryImpl
import com.zoku.data.repository.DataStoreRepository
import com.zoku.data.repository.DataStoreRepositoryImpl
import com.zoku.data.repository.ExpRepository
import com.zoku.data.repository.ExpRepositoryImpl
import com.zoku.data.repository.GroupRepository
import com.zoku.data.repository.GroupRepositoryImpl
import com.zoku.data.repository.LoginRepository
import com.zoku.data.repository.LoginRepositoryImpl
import com.zoku.data.repository.RouteRepository
import com.zoku.data.repository.RouteRepositoryImpl
import com.zoku.data.repository.RunningRepository
import com.zoku.data.repository.RunningRepositoryImpl
import com.zoku.data.repository.TestRepository
import com.zoku.data.repository.TestRepositoryImpl
import com.zoku.data.repository.UserRepository
import com.zoku.data.repository.UserRepositoryImpl
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

    @Binds
    @Singleton
    fun bindExpRepository(
        expRepositoryImpl: ExpRepositoryImpl
    ) : ExpRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    @Singleton
    fun bindGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl
    ) : GroupRepository

    @Binds
    @Singleton
    fun bindAttendanceRepository(
        attendanceRepositoryImpl: AttendanceRepositoryImpl
    ) : AttendanceRepository

    @Binds
    @Singleton
    fun bindRouteRepository(
        routeRepositoryImpl: RouteRepositoryImpl
    ) : RouteRepository
}