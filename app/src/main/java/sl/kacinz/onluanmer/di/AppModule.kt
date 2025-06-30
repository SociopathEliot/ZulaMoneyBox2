package sl.kacinz.onluanmer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import sl.kacinz.onluanmer.data.local.dao.GoalDao
import sl.kacinz.onluanmer.data.local.dao.TransactionDao
import sl.kacinz.onluanmer.data.local.db.AppDatabase
import sl.kacinz.onluanmer.data.repository.GoalRepositoryImpl
import sl.kacinz.onluanmer.data.repository.TransactionRepositoryImpl
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import sl.kacinz.onluanmer.domain.repository.TransactionRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideGoalDao(db: AppDatabase): GoalDao = db.goalDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideGoalRepository(dao: GoalDao): GoalRepository = GoalRepositoryImpl(dao)

    @Provides
    fun provideTransactionRepository(dao: TransactionDao): TransactionRepository = TransactionRepositoryImpl(dao)
}
