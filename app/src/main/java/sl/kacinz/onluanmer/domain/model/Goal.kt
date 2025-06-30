package sl.kacinz.onluanmer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val targetAmount: String,
    val date: String,
    val imageUri: String,
    val currentAmount: Int = 0
): Serializable