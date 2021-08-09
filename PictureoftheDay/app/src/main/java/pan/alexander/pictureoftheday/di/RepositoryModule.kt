package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.data.epic.EpicRepositoryImpl
import pan.alexander.pictureoftheday.data.mars.MarsRepositoryImpl
import pan.alexander.pictureoftheday.data.picture.PictureRepositoryImpl
import pan.alexander.pictureoftheday.data.settings.SettingsRepositoryImpl
import pan.alexander.pictureoftheday.domain.epic.EpicRepository
import pan.alexander.pictureoftheday.domain.mars.MarsRepository
import pan.alexander.pictureoftheday.domain.pod.PictureRepository
import pan.alexander.pictureoftheday.domain.settings.SettingsRepository

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun providePhotoRepository(pictureRepository: PictureRepositoryImpl): PictureRepository

    @Binds
    abstract fun provideSettingsRepository(settingRepository: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun provideEpicRepository(epicRepository: EpicRepositoryImpl): EpicRepository

    @Binds
    abstract fun provideMarsRepository(marsRepository: MarsRepositoryImpl): MarsRepository
}
