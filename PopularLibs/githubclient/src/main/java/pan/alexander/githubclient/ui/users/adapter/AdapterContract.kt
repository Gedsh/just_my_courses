package pan.alexander.githubclient.ui.users.adapter

import pan.alexander.githubclient.domain.entities.GithubUser

class AdapterContract {

    interface ItemView {
        var itemPosition: Int
    }

    interface UserItemView : ItemView {
        fun setLogin(text: String)
    }

    interface ListPresenter<V : ItemView> {
        var onItemClickListener: ((V) -> Unit)?
        fun onBindView(view: V)
        fun onGetCount(): Int
    }

    abstract class UserListPresenter : ListPresenter<UserItemView> {
        val users = mutableListOf<GithubUser>()
    }
}
