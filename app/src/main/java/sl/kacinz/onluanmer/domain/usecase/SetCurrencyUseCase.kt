package sl.kacinz.onluanmer.domain.usecase

import sl.kacinz.onluanmer.domain.repository.SettingsRepository
import javax.inject.Inject

class SetCurrencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(currency: String) = repository.setCurrency(currency)
}
