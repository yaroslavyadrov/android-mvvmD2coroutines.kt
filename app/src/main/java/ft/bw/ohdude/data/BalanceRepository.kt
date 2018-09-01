package ft.bw.ohdude.data

import ft.bw.ohdude.domain.Balance
import kotlinx.coroutines.experimental.delay

class BalanceRepository {

    suspend fun getCurrentCount(): Int {
        delay(400)
        val balance = Balance()
        return 0
    }
}