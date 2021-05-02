package pan.alexander.calculator.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CalculatorData implements Parcelable {
    private final String screenData;

    public CalculatorData(@NonNull String screenData) {
        this.screenData = screenData;
    }

    protected CalculatorData(Parcel in) {
        screenData = in.readString();
    }

    public static final Creator<CalculatorData> CREATOR = new Creator<CalculatorData>() {
        @Override
        public CalculatorData createFromParcel(Parcel in) {
            return new CalculatorData(in);
        }

        @Override
        public CalculatorData[] newArray(int size) {
            return new CalculatorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(screenData);
    }

    public String getScreenData() {
        return screenData;
    }

    @Override
    public String toString() {
        return "CalculatorData{" +
                "screenData='" + screenData + '\'' +
                '}';
    }
}
