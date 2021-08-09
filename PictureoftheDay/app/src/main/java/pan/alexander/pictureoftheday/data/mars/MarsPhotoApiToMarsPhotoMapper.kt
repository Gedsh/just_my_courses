package pan.alexander.pictureoftheday.data.mars

import pan.alexander.pictureoftheday.data.mars.model.MarsPhotoApi
import pan.alexander.pictureoftheday.domain.mars.entities.MarsPhoto
import javax.inject.Inject

class MarsPhotoApiToMarsPhotoMapper @Inject constructor() {
    fun map(marsPhotoApi: MarsPhotoApi): MarsPhoto {
        return MarsPhoto(
            rover = marsPhotoApi.rover.name ?: "",
            url = marsPhotoApi.imageSource
        )
    }
}
