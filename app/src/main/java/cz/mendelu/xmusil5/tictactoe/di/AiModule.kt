package cz.mendelu.xmusil5.tictactoe.di

import android.content.Context
import cz.mendelu.xmusil5.tictactoe.ai.AiConstants
import cz.mendelu.xmusil5.tictactoe.ai.AiPlayerBrainImpl
import cz.mendelu.xmusil5.tictactoe.ai.IAiPlayerBrain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class AiModule {

    @Provides
    @ActivityRetainedScoped
    fun provideAiPlayerBrain(@ApplicationContext context: Context): IAiPlayerBrain{
        return AiPlayerBrainImpl(
            modelPath = AiConstants.MODEL_PATH,
            context = context
        )
    }
}