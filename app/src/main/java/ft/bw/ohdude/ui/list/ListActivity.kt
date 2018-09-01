package ft.bw.ohdude.ui.list

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Switch
import ft.bw.oh_dude.R
import ft.bw.ohdude.base.BaseActivity
import ft.bw.ohdude.domain.Balance
import ft.bw.ohdude.domain.ExpectedOperation
import ft.bw.ohdude.domain.OperationType
import ft.bw.ohdude.domain.addOperation
import ft.bw.ohdude.util.bindView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine

class ListActivity : BaseActivity() {

    lateinit var presenter: ListActivityPresenter

    private val operationsRecyclerView by bindView<RecyclerView>(R.id.operationsRecyclerView)
    private val fab by bindView<FloatingActionButton>(R.id.fab)

    val balance = Balance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)
        val operationsAdapter = OperationsAdapter()
        operationsRecyclerView.layoutManager = LinearLayoutManager(this)
        operationsRecyclerView.adapter = operationsAdapter
        fab.setOnClickListener {
            launch(UI) {
                val newOperation = askForCreateNewOperation("Новая операция")
                balance.addOperation(newOperation.type, newOperation.expected)
                operationsAdapter.submitList(balance.operationsList)
            }
        }
    }

    private suspend fun askForCreateNewOperation(title: String): ExpectedOperation {
        return suspendCoroutine { continuation ->
            val view = LayoutInflater.from(this).inflate(R.layout.add_dialog_view, null, false)
            val typeSwitch = view.findViewById<Switch>(R.id.typeSwitch)
            val amountEditText = view.findViewById<EditText>(R.id.amountEditText)
            AlertDialog.Builder(this)
                    .setTitle(title)
                    .setView(view)
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ ->
                        val type = if (typeSwitch.isChecked) {
                            OperationType.ADD
                        } else {
                            OperationType.REMOVE
                        }
                        val operation = ExpectedOperation(
                                type,
                                amountEditText.text.toString().toInt()
                        )
                        continuation.resume(operation)
                        dialog.dismiss()
                    }
                    .show()
        }
    }
}
