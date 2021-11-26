package pan.alexander.fileconverter.di

import dagger.Binds
import dagger.Module
import pan.alexander.fileconverter.data.MainDataSource
import pan.alexander.fileconverter.data.MainDataSourceImpl

@Module
abstract class DataSourceModule {

    @Binds
    abstract fun provideMainDataSource(mainDataSource: MainDataSourceImpl): MainDataSource
}
