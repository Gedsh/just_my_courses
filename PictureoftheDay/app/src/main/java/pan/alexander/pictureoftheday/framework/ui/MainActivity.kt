package pan.alexander.pictureoftheday.framework.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.MainActivityBinding
import pan.alexander.pictureoftheday.domain.settings.AppTheme
import pan.alexander.pictureoftheday.domain.settings.AppUiMode
import pan.alexander.pictureoftheday.domain.settings.SettingsAction

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainActivityViewModel::class.java) }
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        setAppUiMode(viewModel.getAppUiMode())

        setAppTheme(viewModel.getAppTheme())

        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAppTheme(appTheme: AppTheme) {
        when (appTheme) {
            AppTheme.PURPLE -> setTheme(R.style.Theme_PictureOfTheDay_Purple)
            AppTheme.PINK -> setTheme(R.style.Theme_PictureOfTheDay_Pink)
            AppTheme.INDIGO -> setTheme(R.style.Theme_PictureOfTheDay_Indigo)
        }
    }

    private fun setAppUiMode(appUiMode: AppUiMode) {
        when (appUiMode) {
            AppUiMode.MODE_AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppUiMode.MODE_DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppUiMode.MODE_NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (!isMainScreen()) {
            switchToSecondaryBottomAppBar()
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appBarFav -> {
                val navController = binding.navHostFragmentActivityMain.findNavController()
                navController.navigate(R.id.navigation_viewpager)
            }
            R.id.appBarSettings -> {
                val navController = binding.navHostFragmentActivityMain.findNavController()
                navController.navigate(R.id.navigation_settings)
            }
            R.id.appBarSearch -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(supportFragmentManager, "tag")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        setSupportActionBar(binding.bottomAppBar)

        setFabClickListener()

        observeUiSettingChanges()

        observeErrorMessages()
    }

    private fun setFabClickListener() {
        binding.fabMainActivity.setOnClickListener {
            if (isMainScreen()) {
                switchToSecondaryBottomAppBar()
                setMainScreen(false)
            } else {
                switchToMainBottomAppBar()
                setMainScreen(true)
            }
        }
    }

    private fun observeUiSettingChanges() {
        viewModel.getSettingsChangedLiveData().distinctUntilChanged().observe(this) { action ->
            when (action) {
                is SettingsAction.AppThemeChanged -> {
                    if (viewModel.currentTheme != action.appTheme) {
                        setAppTheme(action.appTheme)
                        viewModel.currentTheme = action.appTheme
                        recreate()
                    }
                }
                is SettingsAction.AppUiModeChanged -> {
                    if (viewModel.currentUiMode != action.appUiMode) {
                        setAppUiMode(action.appUiMode)
                        viewModel.currentUiMode = action.appUiMode
                        recreate()
                    }
                }
            }
        }
    }

    private fun observeErrorMessages() {
        viewModel.getErrorMessageLiveData().observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun isMainScreen() = viewModel.isMain

    private fun setMainScreen(isMainScreen: Boolean) {
        viewModel.isMain = isMainScreen
    }

    private fun switchToMainBottomAppBar() = with(binding) {
        bottomAppBar.navigationIcon =
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_hamburger_menu_bottom_bar
            )
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        fabMainActivity.setImageDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_plus_fab
            )
        )
        bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
    }

    private fun switchToSecondaryBottomAppBar() = with(binding) {
        bottomAppBar.navigationIcon = null
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        fabMainActivity.setImageDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_back_fab
            )
        )
        bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
    }

}
