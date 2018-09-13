package ft.bw.ohdude.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class BalanceTest {

    @Test
    fun `plus operation test`() {
        val balance = Balance()
        assertEquals(0, balance.getCurrentActualBalance())
        balance.addOperation(OperationType.ADD, 100)
        assertEquals(100, balance.getExpectedBalance(System.currentTimeMillis() + 1))
        balance.addOperation(OperationType.ADD, 100)
        assertEquals(200, balance.getExpectedBalance(System.currentTimeMillis() + 1))
    }

    @Test
    fun `balance test`() {
        val balance = Balance()
        assertEquals(0, balance.getCurrentActualBalance())
        balance.addOperation(OperationType.ADD, 100)
        assertEquals(100, balance.getExpectedBalance(System.currentTimeMillis() + 1))
        balance.addOperation(OperationType.ADD, 100)
        assertEquals(200, balance.getExpectedBalance(System.currentTimeMillis() + 1))
        balance.addOperation(OperationType.REMOVE, 100)
        assertEquals(100, balance.getExpectedBalance(System.currentTimeMillis() + 1))
    }

    @Test
    fun `integrity test`() {
        val balance = Balance()
        balance.addOperation(OperationType.ADD, 100)
        balance.addAmountToOperation(balance.operationsList.last().identifier, 50)
        assertEquals(3, balance.operationsList.size)
    }

    @Test
    fun `overspent test`() {
        val balance = Balance()
        balance.addOperation(OperationType.ADD, 5000)
        balance.addAmountToOperation(balance.operationsList.last().identifier, 5000)
        balance.addOperation(OperationType.REMOVE, 4000)
        balance.addAmountToOperation(balance.operationsList.last().identifier, 5000)
        assertEquals(1000, balance.getTotalOverspent())
    }
}
