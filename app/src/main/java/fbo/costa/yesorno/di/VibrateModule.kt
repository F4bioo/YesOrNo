package fbo.costa.yesorno.di

import android.content.Context
import android.os.Vibrator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fbo.costa.yesorno.util.VibrateUtil
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VibrateModule {

    @Singleton
    @Provides
    fun provideVibrator(
        @ApplicationContext context: Context
    ): Vibrator {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Singleton
    @Provides
    fun provideVibrateUtil(
        vibrator: Vibrator
    ): VibrateUtil {
        return VibrateUtil(vibrator)
    }
}
