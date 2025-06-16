package soltani.code.taskvine.di


import android.app.Application
import android.content.Context
import soltani.code.taskvine.account.GoogleAuthClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soltani.code.taskvine.model.AchievementDao
import soltani.code.taskvine.model.CategoryDao
import soltani.code.taskvine.model.TaskDao
import soltani.code.taskvine.model.TaskDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    // Provide the Application context
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context = application


    @Provides
    @Singleton
    fun provideTaskDatabase(application: Context): TaskDatabase {
        return TaskDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: TaskDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideAchievementDao(database: TaskDatabase): AchievementDao {
        return database.achievementDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.getTaskDao()
    }

    @Provides
    @Singleton
    fun provideGoogleAuthClient(application: Context): GoogleAuthClient {
        return GoogleAuthClient(application)
    }
}
