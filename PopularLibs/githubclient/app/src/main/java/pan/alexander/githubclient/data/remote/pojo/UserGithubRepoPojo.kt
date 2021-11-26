package pan.alexander.githubclient.data.remote.pojo

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserGithubRepoPojo(

    @SerializedName("id") val id: Long?,

    @SerializedName("name") val name: String?,

    @SerializedName("owner") val owner: UserGithubRepoOwnerPojo?,

    @SerializedName("description") val description: String?,

    @SerializedName("created_at") val createdAt: String?,

    @SerializedName("updated_at") val updatedAt: String?,

    @SerializedName("forks_count") val forksCount: Long?,

    @SerializedName("open_issues_count") val openIssuesCount: Long?,

    @SerializedName("topics") val topics: List<String>?,

    @SerializedName("watchers") val watchers: Long?
)
