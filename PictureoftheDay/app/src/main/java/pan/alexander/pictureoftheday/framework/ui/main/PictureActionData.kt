package pan.alexander.pictureoftheday.framework.ui.main

import pan.alexander.pictureoftheday.domain.pod.entities.NasaPicture

sealed class PictureActionData {
    data class Success(val nasaPicture: NasaPicture) : PictureActionData()
    data class Error(val error: Throwable) : PictureActionData()
    object Loading : PictureActionData()
}
