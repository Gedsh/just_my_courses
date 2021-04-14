package pan.alexander.simpleweather.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pan.alexander.simpleweather.R
import pan.alexander.simpleweather.presentation.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
