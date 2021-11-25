package pan.alexander.logintask.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import pan.alexander.logintask.R
import pan.alexander.logintask.databinding.ActivityLoginBinding
import pan.alexander.logintask.ui.App
import pan.alexander.logintask.utils.hideSoftKeyboard
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), Contract.View {
    @Inject
    lateinit var presenter: Contract.Presenter

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateActivityView()

        restoreOrInjectPresenter()

        initButtonsClickListeners()
    }

    private fun inflateActivityView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun restoreOrInjectPresenter() {
        (lastCustomNonConfigurationInstance as? Contract.Presenter)?.let {
            presenter = it
        } ?: App.instance.daggerComponent.inject(this)

        presenter.onAttach(this)
    }

    private fun initButtonsClickListeners() {
        initLoginButtonOnClickListener()
        initSignUpButtonOnClickListener()
        initForgetPasswordOnClickListener()
    }

    private fun initLoginButtonOnClickListener() {
        binding.loginButton.setOnClickListener {
            presenter.onLogIn(getInputLogin(), getInputPassword())
        }
    }

    private fun initSignUpButtonOnClickListener() {
        binding.signupButton.setOnClickListener {
            presenter.onSignUp(getInputLogin(), getInputPassword())
        }
    }

    private fun initForgetPasswordOnClickListener() {
        binding.forgetPasswordButton.setOnClickListener {
            presenter.onForgetPassword(getInputLogin())
        }
    }

    private fun getInputLogin(): String {
        return binding.loginInputEditText.text.toString()
    }

    private fun getInputPassword(): String {
        return binding.passwordInputEditText.text.toString()
    }

    override fun setState(state: Contract.ViewState) {
        when (state) {
            is Contract.ViewState.Idle -> setIdleState()
            is Contract.ViewState.Checking -> setCheckingState()
            is Contract.ViewState.Success -> setSuccessState()
            is Contract.ViewState.Error -> setErrorState(state.type)
        }
    }

    private fun setIdleState() = with(binding) {
        loginGroup.visibility = View.VISIBLE
        progressIndicator.visibility = View.GONE
        successMessageCardView.visibility = View.GONE
    }

    private fun setCheckingState() = with(binding) {
        loginGroup.visibility = View.VISIBLE
        progressIndicator.visibility = View.VISIBLE
        successMessageCardView.visibility = View.GONE
    }

    private fun setSuccessState() = with(binding) {
        loginGroup.visibility = View.GONE
        progressIndicator.visibility = View.GONE
        successMessageCardView.visibility = View.VISIBLE
    }

    private fun setErrorState(errorType: Contract.ErrorType) = with(binding) {
        showErrorToast(errorType)

        loginGroup.visibility = View.VISIBLE
        progressIndicator.visibility = View.GONE
        successMessageCardView.visibility = View.GONE
    }

    private fun showErrorToast(errorType: Contract.ErrorType) {
        when (errorType) {
            Contract.ErrorType.USER_ALREADY_EXIST -> showToast(R.string.user_already_exist)
            Contract.ErrorType.USER_NOT_EXIST -> showToast(R.string.user_not_exist)
        }
    }

    private fun showToast(@StringRes textId: Int) {
        Toast.makeText(this, textId, Toast.LENGTH_LONG).show()
    }

    override fun setPassword(password: String) {
        binding.passwordInputEditText.setText(password)
    }

    override fun hideKeyboard() {
        hideSoftKeyboard()
    }

    override fun onStop() {
        super.onStop()

        if (!isChangingConfigurations) {
            presenter.onDestroy()
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = presenter

}
