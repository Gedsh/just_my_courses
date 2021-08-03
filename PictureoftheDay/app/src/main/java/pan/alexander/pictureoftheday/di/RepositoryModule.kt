package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.data.picture.PictureRepositoryImpl
import pan.alexander.pictureoftheday.data.settings.SettingsRepositoryImpl
import pan.alexander.pictureoftheday.domain.picture.PictureRepository
import pan.alexander.pictureoftheday.domain.settings.SettingsRepository

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun providePhotoRepository(pictureRepository: PictureRepositoryImpl): PictureRepository

    @Binds
    abstract fun provideSettingsRepository(settingRepository: SettingsRepositoryImpl): SettingsRepository
}
