package sl.kacinz.onluanmer.domain.usecase

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.repository.SettingsRepository
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<String?> = repository.getCurrency()
}
