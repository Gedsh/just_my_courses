package pan.alexander.pictureoftheday.framework.ui.viewpager.earth

import pan.alexander.pictureoftheday.domain.epic.entities.EpicImage

sealed class EpicActionData {
    data class Success(val epicImage: EpicImage) : EpicActionData()
    data class Error(val error: Throwable) : EpicActionData()
    object Loading : EpicActionData()
}
