package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.data.picture.PictureDataSource
import pan.alexander.pictureoftheday.data.picture.PictureDataSourceImpl

@Module
abstract class DataSourcesModule {
    @Binds
    abstract fun providePhotoDataSource(pictureDataSource: PictureDataSourceImpl): PictureDataSource
}
