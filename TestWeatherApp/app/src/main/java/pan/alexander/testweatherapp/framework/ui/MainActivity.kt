package pan.alexander.testweatherapp.framework.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pan.alexander.testweatherapp.R
import pan.alexander.testweatherapp.framework.ui.main.MainFragment

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
