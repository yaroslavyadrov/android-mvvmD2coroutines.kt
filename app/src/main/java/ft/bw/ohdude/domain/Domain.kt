package ft.bw.ohdude.domain

private val FIRST_OPERATION = ExpectedOperation(
        type = OperationType.ADD,
        expected = 0,
        previousItemIdentifier = ""
)

data class Balance(
        val operationsList: MutableList<ExpectedOperation> = mutableListOf(FIRST_OPERATION)
)

fun Balance.addOperation(type: OperationType, amount: Int) {
    val previousItem = operationsList.last()
    val previousHash = previousItem.identifier
    val operation = ExpectedOperation(
            type,
            amount,
            previousHash
    )
    operationsList.add(operation)
}

fun Balance.getCurrentActualBalance(): Int {
    return operationsList.fold(0) { acc: Int, expectedOperation: ExpectedOperation ->
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
    return operationsList.subList(0, actualIndex + 1).fold(0) { acc: Int, expectedOperation: ExpectedOperation ->
        when (expectedOperation.type) {
            OperationType.ADD -> acc + expectedOperation.expected
            OperationType.REMOVE -> acc - expectedOperation.expected
        }
    }
}

private inline fun Balance.performActionOverOperation(operationId: String, amount: Int, mutator: (ExpectedOperation) -> ExpectedOperation) {
    if (amount < 0) {
        throw Exception("You can pass only positive amount")
    }
    val index = operationsList.indexOfLast { it.identifier == operationId }
    when (index) {
        -1 -> throw Exception("Operation(id = $operationId) not found")
        0 -> throw Exception("You cannot change this operation")
        operationsList.lastIndex -> {
            operationsList[index] = mutator(operationsList.last())
        }
        else -> {
            val renewOperation = mutator(operationsList[index])
            operationsList.add(renewOperation)
            val nextIndex = index + 1
            val previousIndex = index - 1
            operationsList.removeAt(index)
            operationsList[nextIndex] = operationsList[nextIndex].copy(
                    previousItemIdentifier = operationsList[previousIndex].identifier
            )
        }
    }
}

fun Balance.addAmountToOperation(operationId: String, amount: Int) {
    performActionOverOperation(operationId, amount) {
        it.copy(
                actual = it.actual + amount,
                timestamp = System.currentTimeMillis(),
                previousItemIdentifier = operationsList.last().identifier
        )
    }
}

fun Balance.removeAmountFromOperation(operationId: String, amount: Int) {
    performActionOverOperation(operationId, amount) {
        it.copy(
                actual = it.actual - amount,
                timestamp = System.currentTimeMillis(),
                previousItemIdentifier = operationsList.last().identifier
        )
    }
}

data class ExpectedOperation(
        val type: OperationType,
        val expected: Int,
        val previousItemIdentifier: String,
        val actual: Int = 0,
        val timestamp: Long = System.currentTimeMillis()
)

val ExpectedOperation.identifier
    get() = timestamp.toString()//fixme

enum class OperationType {
    ADD,
    REMOVE
}