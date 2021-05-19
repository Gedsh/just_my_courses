package pan.alexander.calculator.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"expression"}, unique = true)})
public class HistoryData implements Parcelable {
    private final String expression;
    private final String result;
    @PrimaryKey
    private final long time;

    public HistoryData(@NonNull String expression, @NonNull String result, long time) {
        this.expression = expression;
        this.result = result;
        this.time = time;
    }

    protected HistoryData(Parcel in) {
        expression = in.readString();
        result = in.readString();
        time = in.readLong();
    }

    public static final Creator<HistoryData> CREATOR = new Creator<HistoryData>() {
        @Override
        public HistoryData createFromParcel(Parcel in) {
            return new HistoryData(in);
        }

        @Override
        public HistoryData[] newArray(int size) {
            return new HistoryData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expression);
        dest.writeString(result);
        dest.writeLong(time);
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public long getTime() {
        return time;
    }

    @Override
    @NonNull
    public String toString() {
        return "HistoryData{" +
                "expression='" + expression + '\'' +
                ", result='" + result + '\'' +
                ", time=" + time +
                '}';
    }
}
