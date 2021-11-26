package pan.alexander.githubclient.di.app

import android.widget.ImageView
import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.web.GlideImageLoader
import pan.alexander.githubclient.utils.image.ImageLoader

@Module
abstract class ImageLoaderModule {

    @Binds
    abstract fun provideImageLoader(imageLoader: GlideImageLoader): ImageLoader<ImageView>
}
