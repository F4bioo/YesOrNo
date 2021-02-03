package fbo.costa.yesorno.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fbo.costa.yesorno.data.api.ApiService
import fbo.costa.yesorno.repository.MainRepository
import fbo.costa.yesorno.util.NetworkMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        apiService: ApiService,
        networkMapper: NetworkMapper
    ): MainRepository {
        return MainRepository(
            apiService,
            networkMapper
        )
    }
}
