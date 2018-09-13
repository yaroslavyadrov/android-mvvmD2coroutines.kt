package ft.bw.ohdude.ui.list

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import ft.bw.oh_dude.R
import ft.bw.ohdude.base.BaseActivity
import ft.bw.ohdude.domain.Balance
import ft.bw.ohdude.domain.OperationType
import ft.bw.ohdude.domain.addAmountToOperation
import ft.bw.ohdude.domain.addOperation
import ft.bw.ohdude.domain.getCurrentActualBalance
import ft.bw.ohdude.domain.getExpectedBalance
import ft.bw.ohdude.domain.identifier
import ft.bw.ohdude.domain.removeAmountFromOperation
import ft.bw.ohdude.util.bindView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine

class ListActivity : BaseActivity() {

    lateinit var presenter: ListActivityPresenter

    private val operationsRecyclerView by bindView<RecyclerView>(R.id.operationsRecyclerView)
    private val fab by bindView<FloatingActionButton>(R.id.fab)
    private val balanceTextView by bindView<TextView>(R.id.balanceTextView)

    val balance = Balance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)
        val operationsAdapter = OperationsAdapter()
        operationsRecyclerView.layoutManager = LinearLayoutManager(this)
        operationsRecyclerView.adapter = operationsAdapter
        fab.setOnClickListener {
            launch(UI) {
                val (type, amount) = try {
                    askForCreateNewOperation()
                } catch (e: Exception) {
                    return@launch
                }
                balance.addOperation(type, amount)
                operationsAdapter.submitList(balance.operationsList)
                operationsAdapter.notifyDataSetChanged()
                balanceTextView.text = "${balance.getCurrentActualBalance()}/${balance.getExpectedBalance(System.currentTimeMillis() + 100)}"
            }
        }
        operationsAdapter.onClick { operation ->
            launch(UI) {
                val (type, amount) = try {
                    askForChangeOperation()
                } catch (e: Exception) {
                    return@launch
                }
                when (type) {
                    OperationType.ADD -> balance.addAmountToOperation(operation.identifier, amount)
                    OperationType.REMOVE -> balance.removeAmountFromOperation(operation.identifier, amount)
                }
                operationsAdapter.submitList(balance.operationsList)
                operationsAdapter.notifyDataSetChanged()
                balanceTextView.text = "${balance.getCurrentActualBalance()}/${balance.getExpectedBalance(System.currentTimeMillis() + 100)}"
            }
        }
    }

    private suspend fun askForCreateNewOperation(): Pair<OperationType, Int> {
        return suspendCoroutine { continuation ->
            val view = LayoutInflater.from(this).inflate(R.layout.add_dialog_view, null, false)
            val typeSwitch = view.findViewById<Switch>(R.id.typeSwitch)
            val amountEditText = view.findViewById<EditText>(R.id.amountEditText)
            AlertDialog.Builder(this)
                    .setTitle("Новая операция")
                    .setView(view)
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ ->
                        val type = if (typeSwitch.isChecked) {
                            OperationType.ADD
                        } else {
                            OperationType.REMOVE
                        }
                        val amount = amountEditText.text.toString().toInt()
                        continuation.resume(type to amount)
                        dialog.dismiss()
                    }
                    .show()
        }
    }

    private suspend fun askForChangeOperation(): Pair<OperationType, Int> {
        return suspendCoroutine { continuation ->
            val view = LayoutInflater.from(this).inflate(R.layout.add_dialog_view, null, false)
            val amountEditText = view.findViewById<EditText>(R.id.amountEditText)
            AlertDialog.Builder(this)
                    .setTitle("Изменить")
                    .setView(view)
                    .setCancelable(true)
                    .setPositiveButton("OK") { dialog, _ ->
                        val amount = amountEditText.text.toString().toInt()
                        continuation.resume(OperationType.ADD to amount)
                        dialog.dismiss()
                    }
                    .show()
        }
    }
}
