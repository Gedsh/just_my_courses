package pan.alexander.pictureoftheday.framework.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.databinding.SettingsFragmentBinding
import pan.alexander.pictureoftheday.domain.settings.AppUiMode
import pan.alexander.pictureoftheday.domain.settings.AppTheme

class SettingsFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)

        setThemeChipChecked()
        setUiModeChipChecked()

        return binding.root
    }

    private fun setThemeChipChecked() {
        when (viewModel.getAppTheme()) {
            AppTheme.PURPLE -> binding.chipGroupAppTheme.check(R.id.chipSettingsThemePurple)
            AppTheme.PINK -> binding.chipGroupAppTheme.check(R.id.chipSettingsThemePink)
            AppTheme.INDIGO -> binding.chipGroupAppTheme.check(R.id.chipSettingsThemeIndigo)
        }
    }

    private fun setUiModeChipChecked() {
        when (viewModel.getAppUiMode()) {
            AppUiMode.MODE_AUTO -> binding.chipGroupAppMode.check(R.id.chipSettingsModeAuto)
            AppUiMode.MODE_DAY -> binding.chipGroupAppMode.check(R.id.chipSettingsModeDay)
            AppUiMode.MODE_NIGHT -> binding.chipGroupAppMode.check(R.id.chipSettingsModeNight)
        }
    }

    override fun onStart() {
        super.onStart()

        initThemeChangedListener()
        initAppModeChangedListener()
    }

    private fun initThemeChangedListener() {
        binding.chipGroupAppTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipSettingsThemePurple -> changeAppTheme(AppTheme.PURPLE)
                R.id.chipSettingsThemePink -> changeAppTheme(AppTheme.PINK)
                R.id.chipSettingsThemeIndigo -> changeAppTheme(AppTheme.INDIGO)
            }
        }
    }

    private fun changeAppTheme(appTheme: AppTheme) {
        viewModel.setAppTheme(appTheme)
    }

    private fun initAppModeChangedListener() {
        binding.chipGroupAppMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipSettingsModeAuto -> setAppMode(AppUiMode.MODE_AUTO)
                R.id.chipSettingsModeDay -> setAppMode(AppUiMode.MODE_DAY)
                R.id.chipSettingsModeNight -> setAppMode(AppUiMode.MODE_NIGHT)
            }
        }
    }

    private fun setAppMode(appUiMode: AppUiMode) {
        viewModel.setAppMode(appUiMode)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
