package pan.alexander.githubclient.ui.userrepos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.githubclient.databinding.ItemUserRepoBinding
import pan.alexander.githubclient.domain.entities.UserGithubRepo

class ReposAdapter(
    private val repoOnClickListener: (userRepo: UserGithubRepo) -> Unit
) : RecyclerView.Adapter<ReposAdapter.ViewHolder>() {

    private val userRepos by lazy { mutableListOf<UserGithubRepo>() }

    fun updateRepos(repos: List<UserGithubRepo>) {
        userRepos.clear()
        userRepos.addAll(repos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemUserRepoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ).apply {
            itemView.setOnClickListener(this)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userRepos[position])
    }

    override fun getItemCount(): Int = userRepos.size

    inner class ViewHolder(
        private val binding: ItemUserRepoBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(userRepo: UserGithubRepo) {
            binding.userRepoTextView.text = userRepo.name
            userRepo.description.takeIf { it.isNotBlank() }
                ?.let {
                    binding.userRepoDescriptionTextView.text = it
                    binding.userRepoDescriptionTextView.visibility = VISIBLE
                } ?: with(binding) {
                userRepoDescriptionTextView.visibility = GONE
            }

        }

        override fun onClick(v: View?) {
            layoutPosition.takeIf { it != RecyclerView.NO_POSITION }
                ?.let { repoOnClickListener(userRepos[it]) }
        }
    }

}
