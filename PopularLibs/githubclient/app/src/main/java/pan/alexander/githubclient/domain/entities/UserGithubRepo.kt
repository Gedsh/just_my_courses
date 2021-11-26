package pan.alexander.githubclient.domain.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "repositories",
    foreignKeys = [ForeignKey(
        entity = GithubUser::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class UserGithubRepo(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
    @ColumnInfo(name = "forks_count") val forksCount: Long,
    @ColumnInfo(name = "open_issues_count") val openIssuesCount: Long,
    @ColumnInfo(name = "topics") val topics: List<String>,
    @ColumnInfo(name = "watchers") val watchers: Long,
    @ColumnInfo(name = "user_id") val userId: Long
) : Parcelable
