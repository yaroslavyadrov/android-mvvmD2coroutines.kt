package ft.bw.ohdude.domain

private val FIRST_OPERATION = ExpectedOperation(
        type = OperationType.ADD,
        expected = 0
)

data class Balance(
        val operationsList: MutableList<ExpectedOperation> = mutableListOf(FIRST_OPERATION)
)

fun Balance.addOperation(type: OperationType, amount: Int) {
    val operation = ExpectedOperation(type, amount)
    operationsList.add(operation)
}

fun Balance.getCurrentActualBalance(): Int {
    return operationsList
            .filter { it.nextRevisionId.isEmpty() }
            .fold(0) { acc: Int, expectedOperation: ExpectedOperation ->
                when (expectedOperation.type) {
                    OperationType.ADD -> acc + expectedOperation.actual
                    OperationType.REMOVE -> acc - expectedOperation.actual
                }
            }
}

fun Balance.getExpectedBalance(timestamp: Long): Int {
    val index = operationsList.indexOfLast { it.timestamp <= timestamp }
    val actualIndex = if (index == -1) {
        operationsList.lastIndex
    } else {
        index
    }
    return operationsList.subList(0, actualIndex + 1)
            .asSequence()
            .filter { it.nextRevisionId.isEmpty() }
            .fold(0) { acc: Int, expectedOperation: ExpectedOperation ->
                when (expectedOperation.type) {
                    OperationType.ADD -> acc + expectedOperation.expected
                    OperationType.REMOVE -> acc - expectedOperation.expected
                }
            }
}

private tailrec fun Balance.performActionOverOperation(
        operationId: String,
        mutator: (ExpectedOperation) -> ExpectedOperation
) {
    val index = operationsList.indexOfLast { it.identifier == operationId }
    when (index) {
        -1 -> throw Exception("Operation(id = $operationId) not found")
        0 -> throw Exception("You cannot change this operation")
        else -> {
            val previousRevision = operationsList[index]
            if (previousRevision.nextRevisionId.isNotEmpty()) {
                performActionOverOperation(previousRevision.nextRevisionId, mutator)
            } else {
                val newRevision = mutator(previousRevision)
                operationsList.add(newRevision)
                operationsList[index] = previousRevision.copy(
                        nextRevisionId = newRevision.identifier
                )
            }
        }
    }
}

fun Balance.addAmountToOperation(operationId: String, amount: Int) {
    if (amount < 0) {
        throw Exception("You can pass only positive amount")
    }
    performActionOverOperation(operationId) { previousRevision ->
        previousRevision.copy(actual = previousRevision.actual + amount)
    }
}

fun Balance.removeAmountFromOperation(operationId: String, amount: Int) {
    if (amount < 0) {
        throw Exception("You can pass only positive amount")
    }
    performActionOverOperation(operationId) { previousRevision ->
        previousRevision.copy(actual = previousRevision.actual - amount)
    }
}

fun Balance.changeExpectedValueForOperation(operationId: String, newExpected: Int) {
    performActionOverOperation(operationId) { previousOperation ->
        previousOperation.copy(expected = newExpected)
    }
}

data class ExpectedOperation(
        val type: OperationType,
        val expected: Int,
        val previousRevisionId: String = "",
        val nextRevisionId: String = "",
        val actual: Int = 0,
        val timestamp: Long = System.currentTimeMillis()
)

val ExpectedOperation.identifier
    get() = timestamp.toString()//fixme

enum class OperationType {
    ADD,
    REMOVE
}