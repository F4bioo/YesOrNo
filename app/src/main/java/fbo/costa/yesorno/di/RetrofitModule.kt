package fbo.costa.yesorno.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fbo.costa.yesorno.BuildConfig
import fbo.costa.yesorno.data.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideLevel(
    ): HttpLoggingInterceptor.Level {
        return HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpLogging(
        level: HttpLoggingInterceptor.Level
    ): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = level
        return logging
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addInterceptor(logging)
        }
        return client.build()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(
    ): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://yesno.wtf/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }

    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit.Builder
    ): ApiService {
        return retrofit
            .build()
            .create(ApiService::class.java)
    }
}
