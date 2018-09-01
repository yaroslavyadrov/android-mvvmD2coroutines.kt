package ft.bw.ohdude.ui.list

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ft.bw.oh_dude.R
import ft.bw.ohdude.base.BaseActivity
import ft.bw.ohdude.util.bindView

class ListActivity : BaseActivity() {

    lateinit var presenter: ListActivityPresenter

    private val ohDudeButton by bindView<Button>(R.id.ohDudeButton)
    private val daysTextView by bindView<TextView>(R.id.daysTextView)
    private val plusButton by bindView<Button>(R.id.plusButton)
    private val minusButton by bindView<Button>(R.id.minusButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)
    }
}
