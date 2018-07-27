package ft.bw.ohdude.data

import kotlinx.coroutines.experimental.delay

class SampleRepository {

    suspend fun getSomeData(): Int {
        delay(400)
        return 42
    }

}