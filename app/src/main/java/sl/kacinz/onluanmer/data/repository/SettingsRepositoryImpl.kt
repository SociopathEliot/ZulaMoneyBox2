package sl.kacinz.onluanmer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import sl.kacinz.onluanmer.data.local.dao.SettingsDao
import sl.kacinz.onluanmer.data.local.db.AppDatabase
import sl.kacinz.onluanmer.domain.model.AppSettings
import sl.kacinz.onluanmer.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dao: SettingsDao,
    private val db: AppDatabase
) : SettingsRepository {
    override fun getCurrency(): Flow<String?> = dao.getCurrency()

    override suspend fun setCurrency(currency: String) {
        dao.insert(AppSettings(defaultCurrency = currency))
    }

    override suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            db.clearAllTables()
        }

    }
}
