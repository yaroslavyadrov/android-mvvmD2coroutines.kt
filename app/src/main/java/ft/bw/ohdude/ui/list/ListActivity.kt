package ft.bw.ohdude.ui.list

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import ft.bw.oh_dude.R
import ft.bw.ohdude.util.bindView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class ListActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: ListActivityViewModel

    private val ohDudeButton by bindView<Button>(R.id.ohDudeButton)
    private val daysTextView by bindView<TextView>(R.id.daysTextView)
    private val plusButton by bindView<Button>(R.id.plusButton)
    private val minusButton by bindView<Button>(R.id.minusButton)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)
        ohDudeButton.setOnClickListener { viewModel.ohDudeClick() }
        plusButton.setOnClickListener { viewModel.onPlusClick() }
        minusButton.setOnClickListener { viewModel.onMinusClick() }
        viewModel.daysLiveData.observe(this, Observer { daysTextView.text = "$it дней" })
    }
}
