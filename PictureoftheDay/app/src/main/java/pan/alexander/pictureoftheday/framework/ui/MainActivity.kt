package pan.alexander.pictureoftheday.framework.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainActivityViewModel::class.java) }
    private lateinit var binding: MainActivityBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_main)
        )
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
            R.id.appBarFav -> Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show()
            R.id.appBarSettings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
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

        binding.fabMainActivity.setOnClickListener { fabClicked() }

        observeErrorMessages()
    }

    private fun observeErrorMessages() {
        viewModel.getErrorMessageLiveData().observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun fabClicked() {
        if (isMainScreen()) {
            switchToSecondaryBottomAppBar()
            setMainScreen(false)
        } else {
            switchToMainBottomAppBar()
            setMainScreen(true)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentActivityMain)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
