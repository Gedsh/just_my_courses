package pan.alexander.testweatherapp.framework.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.KoinExperimentalAPI
import org.koin.core.scope.Scope
import pan.alexander.testweatherapp.R
import pan.alexander.testweatherapp.framework.ui.main.WeatherFragment

class MainActivity : AppCompatActivity(R.layout.main_activity), AndroidScopeComponent {

    override val scope: Scope by activityRetainedScope()

    @KoinExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {

        setupKoinFragmentFactory(scope)

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace<WeatherFragment>(R.id.container)
                .commitNow()
        }
    }
}
