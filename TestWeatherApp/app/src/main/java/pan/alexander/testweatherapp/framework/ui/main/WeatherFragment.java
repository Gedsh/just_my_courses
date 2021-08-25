package pan.alexander.testweatherapp.framework.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.regex.Pattern;

import kotlin.Lazy;
import pan.alexander.testweatherapp.R;
import pan.alexander.testweatherapp.databinding.WeatherFragmentBinding;
import pan.alexander.testweatherapp.domain.entities.CurrentWeather;

import static org.koin.android.compat.ViewModelCompat.viewModel;
import static pan.alexander.testweatherapp.framework.App.LOG_TAG;

public class WeatherFragment extends Fragment implements View.OnClickListener {
    private static final Pattern digitsPattern = Pattern.compile("\\d+");

    private final Lazy<WeatherViewModel> weatherViewModel = viewModel(this, WeatherViewModel.class);
    private WeatherFragmentBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = WeatherFragmentBinding.inflate(inflater, container, false);
        binding.zipSearchButton.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeWeather();
    }

    private void observeWeather() {
        weatherViewModel.getValue()
                .getCurrentWeatherActionData()
                .observe(getViewLifecycleOwner(), this::renderWeather);
    }

    private void renderWeather(CurrentWeatherActionData weatherActionData) {
        if (weatherActionData instanceof CurrentWeatherActionData.Success) {
            CurrentWeather weather = ((CurrentWeatherActionData.Success) weatherActionData).getCurrentWeather();
            fillFieldsWithValues(weather);
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.weatherFieldsGroup.setVisibility(View.VISIBLE);
        } else if (weatherActionData instanceof CurrentWeatherActionData.Error) {
            Throwable throwable = ((CurrentWeatherActionData.Error) weatherActionData).getThrowable();
            Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "Request weather error", throwable);
            binding.loadingProgressBar.setVisibility(View.GONE);
        } else if (weatherActionData instanceof CurrentWeatherActionData.Loading) {
            binding.loadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void fillFieldsWithValues(CurrentWeather weather) {
        if (!binding.zipSearchInputEditText.isFocused()) {
            binding.zipSearchInputEditText.setText(String.valueOf(weather.getZipCode()));
        }
        binding.textViewLocationValue.setText(weather.getLocation());
        binding.textViewTemperatureValue.setText(String.format(Locale.ENGLISH, "%.2f C", weather.getTemperature()));
        binding.textViewWindValue.setText(String.format(Locale.ENGLISH, "%.2f m/sec", weather.getWindSpeed()));
        binding.textViewHumidityValue.setText(String.format(Locale.ENGLISH, "%d %%", weather.getHumidity()));
        binding.textViewVisibilityValue.setText(weather.getVisibility());
        binding.textViewSunriseValue.setText(weather.getSunriseTime().toString());
        binding.textViewSunsetValue.setText(weather.getSunsetTime().toString());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.zipSearchButton) {
            Editable zipCodeStr = binding.zipSearchInputEditText.getText();
            if (zipCodeStr != null && digitsPattern.matcher(zipCodeStr.toString()).matches()) {
                weatherViewModel.getValue().requestCurrentWeather(Integer.parseInt(zipCodeStr.toString()));
            } else {
                Toast.makeText(requireContext(), R.string.wrong_zip, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

}
