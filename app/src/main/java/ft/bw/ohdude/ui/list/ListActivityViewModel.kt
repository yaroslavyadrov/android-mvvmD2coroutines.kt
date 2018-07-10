package ft.bw.ohdude.ui.list

import android.arch.lifecycle.MutableLiveData
import ft.bw.ohdude.base.BaseViewModel
import ft.bw.ohdude.data.SampleRepository
import kotlinx.coroutines.experimental.launch
import java.lang.Thread.sleep
import javax.inject.Inject

class ListActivityViewModel @Inject constructor(
        private val repository: SampleRepository
) : BaseViewModel() {

    private var days = 0
    val daysLiveData = MutableLiveData<Int>()

    fun ohDudeClick() {
        days = 0
        daysLiveData.postValue(days)
    }

    fun onPlusClick() {
        lateinit var data: String
        launch {
            days += 1
            sleep(400)
            data = repository.getSomeData().toString()
            daysLiveData.postValue(data.toInt() + days)
        }
    }

    fun onMinusClick() {
        launch {
            days -= 1
            val data = repository.getSomeData()
            Thread.sleep(400)
            daysLiveData.postValue(data - days)
        }
    }
}