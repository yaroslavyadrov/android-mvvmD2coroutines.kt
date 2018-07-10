package ft.bw.ohdude.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ft.bw.ohdude.ui.list.ListActivity
import ft.bw.ohdude.ui.list.ListActivityModule

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [ListActivityModule::class])
    internal abstract fun bindListActivity(): ListActivity

}
