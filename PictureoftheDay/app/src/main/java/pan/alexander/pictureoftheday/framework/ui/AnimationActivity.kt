package pan.alexander.pictureoftheday.framework.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.Window

import pan.alexander.pictureoftheday.R

class AnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        activateActivityAnimations()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
    }

    private fun activateActivityAnimations() = with(window) {
        requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        allowEnterTransitionOverlap = true
        allowReturnTransitionOverlap = true
        enterTransition = ChangeBounds()
        exitTransition = ChangeBounds()
    }
}
