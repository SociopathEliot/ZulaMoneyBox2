package sl.kacinz.onluanmer.utils

enum class TimeRange(val label: String, val days: Long?) {
    LAST_WEEK("Last week", 7),
    LAST_MONTH("Last month", 30),
    LAST_YEAR("Last year", 365),
    ALL_TIME("All time", null)
}
