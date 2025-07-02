package sl.kacinz.onluanmer.domain.model

/**
 * Provides a single [Goal] with a predefined set of transactions for testing
 * charts without relying on database data.
 */
object SampleGoal {
    private val balances = listOf(120, 220, 420, 220, 0, 120)
    private val dates = listOf(
        "19.06.2024",
        "20.06.2024",
        "21.06.2024",
        "22.06.2024",
        "23.06.2024",
        "24.06.2024"
    )

    val goal = Goal(
        id = 1,
        name = "Sample Goal",
        targetAmount = 500,
        date = "24.06.2024",
        imageUri = "",
        currentAmount = balances.last()
    )

    /**
     * Transactions are generated dynamically from [balances] so that each entry
     * reflects the change from the previous day.
     */
    val transactions: List<Transaction> = balances.mapIndexed { index, balance ->
        val prev = if (index == 0) 0 else balances[index - 1]
        Transaction(
            goalId = goal.id,
            amount = balance - prev,
            comment = "",
            date = dates[index]
        )
    }
}
