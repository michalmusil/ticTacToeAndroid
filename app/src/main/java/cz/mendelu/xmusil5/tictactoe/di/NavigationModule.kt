package cz.mendelu.xmusil5.tictactoe.di

import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import cz.mendelu.xmusil5.tictactoe.navigation.NavigationRouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationRouter(): INavigationRouter {
        return NavigationRouterImpl()
    }
}