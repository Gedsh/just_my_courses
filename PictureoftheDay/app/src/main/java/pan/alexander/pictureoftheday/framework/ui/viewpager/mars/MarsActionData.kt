package pan.alexander.pictureoftheday.framework.ui.viewpager.mars

import pan.alexander.pictureoftheday.domain.mars.entities.MarsPhoto

sealed class MarsActionData {
    data class Success(val marsPhoto: MarsPhoto) : MarsActionData()
    data class Error(val error: Throwable) : MarsActionData()
    object Loading : MarsActionData()
}
