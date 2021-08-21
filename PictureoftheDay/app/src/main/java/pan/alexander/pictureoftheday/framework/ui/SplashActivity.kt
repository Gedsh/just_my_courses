package pan.alexander.pictureoftheday.framework.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.lifecycle.ViewModelProvider
import pan.alexander.pictureoftheday.databinding.ActivitySplashBinding
import pan.alexander.pictureoftheday.utils.ThemeUtils

class SplashActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SplashActivityViewModel::class.java) }
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeUtils.setAppUiMode(viewModel.getAppUiMode())

        ThemeUtils.setAppTheme(this, viewModel.getAppTheme())

        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateNasaImage()
    }

    private fun animateNasaImage() {
        FlingAnimation(binding.imageViewSplash, DynamicAnimation.ROTATION).apply {
            minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_ROTATION_DEGREES
            setStartVelocity(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    270f,
                    resources.displayMetrics
                )
            )
            setMinValue(0f)
            setMaxValue(360f)
            friction = 0.5f
        }.addEndListener { _, _, _, _ ->
            startMainActivity()
        }.start()
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
