package pan.alexander.githubclient.ui.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import pan.alexander.githubclient.databinding.ItemGithubUserBinding
import pan.alexander.githubclient.utils.image.ImageLoader

class UsersAdapter(
    private val presenter: AdapterContract.UserListPresenter,
    private val imageLoader: ImageLoader<ImageView>
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            itemView.setOnClickListener { presenter.onItemClickListener?.invoke(this) }
        }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        presenter.onBindView(holder.apply { itemPosition = position })
    }

    override fun getItemCount(): Int = presenter.onGetCount()

    inner class ViewHolder(
        private val binding: ItemGithubUserBinding
    ) : RecyclerView.ViewHolder(binding.root), AdapterContract.UserItemView {

        override fun setLogin(text: String) {
            binding.userLoginTextView.text = text
        }

        override fun loadAvatar(url: String) {
            imageLoader.loadInto(url, binding.userAvatarImageView)
        }

        override var itemPosition: Int = RecyclerView.NO_POSITION

    }
}
