package pan.alexander.githubclient.web

import android.widget.ImageView
import com.bumptech.glide.Glide
import pan.alexander.githubclient.R
import pan.alexander.githubclient.utils.image.ImageLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlideImageLoader @Inject constructor() : ImageLoader<ImageView> {
    override fun loadInto(url: String, container: ImageView) {
        Glide.with(container)
            .load(url)
            .placeholder(R.drawable.ic_baseline_cached_24)
            .error(R.drawable.ic_baseline_warning_24)
            .into(container)
    }
}
