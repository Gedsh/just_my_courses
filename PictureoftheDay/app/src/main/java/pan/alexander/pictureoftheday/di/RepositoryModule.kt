package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.data.picture.PictureRepositoryImpl
import pan.alexander.pictureoftheday.domain.picture.PictureRepository

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun providePhotoRepository(pictureRepository: PictureRepositoryImpl): PictureRepository
}
