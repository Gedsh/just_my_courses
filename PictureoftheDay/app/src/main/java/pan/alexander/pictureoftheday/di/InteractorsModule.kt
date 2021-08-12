package pan.alexander.pictureoftheday.di

import dagger.Binds
import dagger.Module
import pan.alexander.pictureoftheday.domain.picture.PictureInteractor
import pan.alexander.pictureoftheday.domain.picture.PictureInteractorImpl

@Module
abstract class InteractorsModule {
    @Binds
    abstract fun providePictureInteractor(pictureInteractor: PictureInteractorImpl): PictureInteractor
}
