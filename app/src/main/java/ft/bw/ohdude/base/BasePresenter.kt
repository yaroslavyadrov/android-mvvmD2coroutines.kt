package ft.bw.ohdude.base

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI

open class BasePresenter {

    private val uiContext = UI + Job()

    private val backgroundContext = CommonPool + Job()
}