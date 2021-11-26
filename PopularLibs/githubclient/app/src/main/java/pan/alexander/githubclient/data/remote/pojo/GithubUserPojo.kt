package pan.alexander.githubclient.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GithubUserPojo(
    @SerializedName("id") val id: Long?,

    @SerializedName("login") val login: String?,

    @SerializedName("avatar_url") val avatarURL: String?,

    @SerializedName("repos_url") val reposURL: String?
)
