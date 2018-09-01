package ft.bw.ohdude.ui.list

import ft.bw.ohdude.base.BasePresenter
import ft.bw.ohdude.data.BalanceRepository
import kotlinx.coroutines.experimental.launch
import java.lang.Thread.sleep

class ListActivityPresenter constructor(
        private val repository: BalanceRepository
) : BasePresenter() {

    private var days = 0

    fun ohDudeClick() {
        days = 0
    }

    fun onPlusClick() {
        lateinit var data: String
        launch {
            days += 1
            sleep(400)
        }
    }

    fun onMinusClick() {
        launch {
            days -= 1
            Thread.sleep(400)
        }
    }
}