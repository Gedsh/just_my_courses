package pan.alexander.fileconverter.di

import dagger.Binds
import dagger.Module
import pan.alexander.fileconverter.utils.files.FileUtils
import pan.alexander.fileconverter.utils.files.FileUtilsImpl
import pan.alexander.fileconverter.utils.rxschedulers.MainSchedulersProvider
import pan.alexander.fileconverter.utils.rxschedulers.SchedulersProvider

@Module
abstract class UtilsModule {

    @Binds
    abstract fun provideRxSchedulers(schedulersProvider: MainSchedulersProvider): SchedulersProvider

    @Binds
    abstract fun provideFileUtils(fileUtils: FileUtilsImpl): FileUtils
}
