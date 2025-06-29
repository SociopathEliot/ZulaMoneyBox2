package sl.kacinz.onluanmer.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import sl.kacinz.onluanmer.data.local.dao.GoalDao
import sl.kacinz.onluanmer.data.local.db.AppDatabase
import sl.kacinz.onluanmer.data.repository.GoalRepositoryImpl
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import sl.kacinz.onluanmer.domain.usecase.CreateGoalUseCase
import sl.kacinz.onluanmer.domain.usecase.GetGoalsUseCase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides @Singleton
  fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
    Room.databaseBuilder(ctx, AppDatabase::class.java, "onluanmer.db")
      .fallbackToDestructiveMigration()
      .build()

  @Provides
  fun provideGoalDao(db: AppDatabase): GoalDao =
    db.goalDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
  @Binds @Singleton
  abstract fun bindGoalRepo(impl: GoalRepositoryImpl): GoalRepository
}

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

  @Provides
  fun provideGetGoalsUC(repo: GoalRepository): GetGoalsUseCase =
    GetGoalsUseCase(repo)

  @Provides
  fun provideCreateGoalUC(repo: GoalRepository): CreateGoalUseCase =
    CreateGoalUseCase(repo)
}
