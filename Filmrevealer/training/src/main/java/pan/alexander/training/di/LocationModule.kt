package pan.alexander.training.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import dagger.Module
import dagger.Provides

@Module
class LocationModule(private val context: Context) {

    @Provides
    fun provideLocationManager() = context.getSystemService(Context.LOCATION_SERVICE) as
            LocationManager

    @Provides
    fun getGeocoder(): Geocoder = Geocoder(context)
}
