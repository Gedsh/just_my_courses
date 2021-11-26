package pan.alexander.githubclient.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserGithubRepoOwnerPojo(
    @SerializedName("id") val id: Long
)
