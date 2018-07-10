package ft.bw.ohdude.ui.list

import dagger.Module
import dagger.Provides
import ft.bw.ohdude.data.SampleRepository

@Module
class ListActivityModule {

    @Provides
    internal fun provideRepository(): SampleRepository = SampleRepository()

    @Provides
    internal fun provideListActivityViewModel(sampleRepository: SampleRepository): ListActivityViewModel {
        return ListActivityViewModel(sampleRepository)
    }
}
