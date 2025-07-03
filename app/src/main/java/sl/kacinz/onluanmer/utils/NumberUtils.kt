package sl.kacinz.onluanmer.utils

import kotlin.math.abs

fun Float.toKString(): String {
    val absValue = abs(this)
    return if (absValue >= 1000f) String.format("%.1f k", this / 1000f) else if (this % 1f == 0f) this.toInt().toString() else String.format("%.1f", this)
}

fun Int.toKString(): String = this.toFloat().toKString()
