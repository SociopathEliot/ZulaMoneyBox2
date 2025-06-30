package sl.kacinz.onluanmer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalId: Int,
    val amount: Int,
    val comment: String,
    val date: String
) : Serializable
