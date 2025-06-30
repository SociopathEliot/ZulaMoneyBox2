package sl.kacinz.onluanmer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE goalId = :goalId ORDER BY id DESC")
    fun getTransactions(goalId: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)
}
