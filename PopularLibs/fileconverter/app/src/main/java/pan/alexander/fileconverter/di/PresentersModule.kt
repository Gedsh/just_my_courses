package pan.alexander.fileconverter.di

import dagger.Binds
import dagger.Module
import pan.alexander.fileconverter.ui.MainContract
import pan.alexander.fileconverter.ui.MainPresenter

@Module
abstract class PresentersModule {

    @Binds
    abstract fun provideMainPresenter(presenter: MainPresenter): MainContract.Presenter
}
