package pan.alexander.calculator.domain;

public interface SettingsRepository {
    String getSettings(String key);
    void  saveSettings(String key, String value);
}
