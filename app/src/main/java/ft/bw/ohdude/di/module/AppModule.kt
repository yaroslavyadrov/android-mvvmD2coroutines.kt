package ft.bw.ohdude.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ft.bw.ohdude.di.ApplicationScope

@Module(includes = [])
class AppModule {

    @Provides
    @ApplicationScope
    fun provideContext(application: Application): Context = application
}
