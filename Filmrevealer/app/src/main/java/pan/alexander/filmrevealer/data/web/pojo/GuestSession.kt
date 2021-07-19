package pan.alexander.filmrevealer.data.web.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GuestSession(
    @SerializedName("success") val success: Boolean,
    @SerializedName("guest_session_id") val guestSessionId: String,
    @SerializedName("expires_at") val expiresAt: String,
)
