package ft.bw.ohdude.ui.list

import ft.bw.ohdude.base.BasePresenter
import ft.bw.ohdude.data.BalanceRepository
import kotlinx.coroutines.experimental.launch
import java.lang.Thread.sleep

class ListActivityPresenter constructor(
        private val repository: BalanceRepository
) : BasePresenter() {

}