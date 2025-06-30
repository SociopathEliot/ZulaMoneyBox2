package sl.kacinz.onluanmer.domain.usecase

import sl.kacinz.onluanmer.domain.repository.SettingsRepository
import javax.inject.Inject

class ClearDataUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() = repository.clearAllData()
}
