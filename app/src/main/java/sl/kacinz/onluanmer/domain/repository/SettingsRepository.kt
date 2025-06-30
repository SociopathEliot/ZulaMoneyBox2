package sl.kacinz.onluanmer.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getCurrency(): Flow<String?>
    suspend fun setCurrency(currency: String)
    suspend fun clearAllData()
}
