package ft.bw.ohdude.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ft.bw.ohdude.App
import ft.bw.ohdude.di.module.ActivityBuilderModule
import ft.bw.ohdude.di.module.AppModule
import javax.inject.Singleton

@Singleton
@ApplicationScope
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityBuilderModule::class
        ]
)
interface AppComponent : AndroidInjector<App>


