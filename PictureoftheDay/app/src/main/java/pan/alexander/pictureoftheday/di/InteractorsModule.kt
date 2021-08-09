package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.domain.epic.EpicInteractor
import pan.alexander.pictureoftheday.domain.epic.EpicInteractorImpl
import pan.alexander.pictureoftheday.domain.mars.MarsInteractor
import pan.alexander.pictureoftheday.domain.mars.MarsInteractorImpl
import pan.alexander.pictureoftheday.domain.pod.PictureInteractor
import pan.alexander.pictureoftheday.domain.pod.PictureInteractorImpl

@Module
abstract class InteractorsModule {
    @Binds
    abstract fun providePictureInteractor(pictureInteractor: PictureInteractorImpl): PictureInteractor

    @Binds
    abstract fun provideEpicInteractor(epicInteractor: EpicInteractorImpl): EpicInteractor

    @Binds
    abstract fun provideMarsInteractor(marsInteractor: MarsInteractorImpl): MarsInteractor
}
