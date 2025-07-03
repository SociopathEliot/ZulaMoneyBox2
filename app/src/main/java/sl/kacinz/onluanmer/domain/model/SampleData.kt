package sl.kacinz.onluanmer.domain.model

object SampleData {
    val sampleGoal = Goal(
        id = -1,
        name = "Demo Car Goal",
        targetAmount = 2000,
        date = "30.06.2024",

        imageUri = "android.resource://xx.example.sample/drawable/ic_safe",
        currentAmount = 1110
    )

    val sampleTransactions = listOf(
        Transaction(goalId = sampleGoal.id, amount = 120, comment = "Deposit for new car", date = "19.06.2024"),
        Transaction(goalId = sampleGoal.id, amount = 220, comment = "Deposit for new car", date = "20.06.2024"),
        Transaction(goalId = sampleGoal.id, amount = 430, comment = "Deposit for new car", date = "21.06.2024"),
        Transaction(goalId = sampleGoal.id, amount = 220, comment = "Deposit for new car", date = "22.06.2024"),
        Transaction(goalId = sampleGoal.id, amount = 0, comment = "No deposit today", date = "23.06.2024"),
        Transaction(goalId = sampleGoal.id, amount = 120, comment = "Deposit for new car", date = "24.06.2024")
    )
}
