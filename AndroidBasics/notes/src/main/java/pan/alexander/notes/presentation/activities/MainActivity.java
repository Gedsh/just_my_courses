package pan.alexander.notes.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pan.alexander.notes.R;
import pan.alexander.notes.presentation.fragments.NotesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, NotesFragment.newInstance())
                    .commitNow();
        }
    }
}
