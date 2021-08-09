package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.data.epic.EpicDataSource
import pan.alexander.pictureoftheday.data.epic.EpicDataSourceImpl
import pan.alexander.pictureoftheday.data.mars.MarsPhotosDataSource
import pan.alexander.pictureoftheday.data.mars.MarsPhotosDataSourceImpl
import pan.alexander.pictureoftheday.data.picture.PictureDataSource
import pan.alexander.pictureoftheday.data.picture.PictureDataSourceImpl

@Module
abstract class DataSourcesModule {
    @Binds
    abstract fun providePhotoDataSource(pictureDataSource: PictureDataSourceImpl): PictureDataSource

    @Binds
    abstract fun provideEpicDataSource(epicDataSource: EpicDataSourceImpl): EpicDataSource

    @Binds
    abstract fun provideMarsDataSource(marsPhotosDataSource: MarsPhotosDataSourceImpl): MarsPhotosDataSource
}
