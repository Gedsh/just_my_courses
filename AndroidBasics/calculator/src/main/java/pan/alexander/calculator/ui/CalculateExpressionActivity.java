package pan.alexander.calculator.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class CalculateExpressionActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null) {
            return;
        }

        CharSequence incomingText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (incomingText == null || incomingText.toString().isEmpty()) {
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, incomingText.toString().replaceAll(" ", ""));
        startActivity(intent);

        finish();
    }
}
