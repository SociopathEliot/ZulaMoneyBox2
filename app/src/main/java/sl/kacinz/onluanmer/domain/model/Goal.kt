package sl.kacinz.onluanmer.domain.model

data class Goal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val imageUri: String? = null,
    val targetDate: Long
)
