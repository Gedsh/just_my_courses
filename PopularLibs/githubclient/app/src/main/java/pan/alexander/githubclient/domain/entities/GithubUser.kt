package pan.alexander.githubclient.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class GithubUser(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "avatar_url") val avatarURL: String,
    @ColumnInfo(name = "repos_url") val reposURL: String
) : Parcelable
