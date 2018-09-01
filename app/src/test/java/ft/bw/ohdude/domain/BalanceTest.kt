package ft.bw.ohdude.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class BalanceTest {

    @Test
    fun `plus operation test`() {
        val balance = Balance()
        assertEquals(balance.getCurrentActualBalance(), 0)
        balance.addOperation(OperationType.ADD, 100)
        balance.addAmountToOperation(balance.operationsList.last().identifier, 50)
        assertEquals(balance.getExpectedBalance(System.currentTimeMillis() + 1), 100)
    }
}