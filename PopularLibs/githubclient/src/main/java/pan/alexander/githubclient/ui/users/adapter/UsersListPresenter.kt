package pan.alexander.githubclient.ui.users.adapter

import javax.inject.Inject

class UsersListPresenter @Inject constructor() : AdapterContract.UserListPresenter() {

    override var onItemClickListener: ((AdapterContract.UserItemView) -> Unit)? = null

    override fun onGetCount() = users.size

    override fun onBindView(view: AdapterContract.UserItemView) {
        val user = users[view.itemPosition]
        view.setLogin(user.login)
    }
}
