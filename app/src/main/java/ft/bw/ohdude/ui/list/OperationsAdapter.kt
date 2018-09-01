package ft.bw.ohdude.ui.list

import android.support.v4.content.ContextCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ft.bw.oh_dude.R
import ft.bw.ohdude.domain.ExpectedOperation
import ft.bw.ohdude.domain.OperationType
import ft.bw.ohdude.util.bindView

class OperationsAdapter : ListAdapter<ExpectedOperation, OperationsAdapter.ViewHolder>(ExpectedOperationCallback()) {

    private var onClickListener: (ExpectedOperation) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.operation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun onClick(block: (ExpectedOperation) -> Unit) {
        onClickListener = block
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val expectedValueTextView by bindView<TextView>(R.id.expectedValueTextView)
        private val actualValueTextView by bindView<TextView>(R.id.actualValueTextView)
        private val typeTextView by bindView<TextView>(R.id.typeTextView)
        private val debugTextView by bindView<TextView>(R.id.debugTextView)

        fun bind(operation: ExpectedOperation) {
            itemView.setOnClickListener {
                onClickListener(operation)
            }
            with(operation) {
                expectedValueTextView.text = expected.toString()
                actualValueTextView.text = actual.toString()
                typeTextView.text = type.name
                val typeTextColor = when (type) {
                    OperationType.ADD -> ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
                    OperationType.REMOVE -> ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
                }
                typeTextView.setTextColor(typeTextColor)
                debugTextView.text = operation.toString()
            }
        }
    }

    class ExpectedOperationCallback : DiffUtil.ItemCallback<ExpectedOperation>() {
        override fun areItemsTheSame(oldItem: ExpectedOperation, newItem: ExpectedOperation): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExpectedOperation, newItem: ExpectedOperation): Boolean {
            return oldItem.timestamp == newItem.timestamp &&
                    oldItem.expected == newItem.expected &&
                    oldItem.previousRevisionId == newItem.previousRevisionId &&
                    oldItem.nextRevisionId == newItem.nextRevisionId
        }
    }
}