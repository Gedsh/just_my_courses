package pan.alexander.calculator.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;

import pan.alexander.calculator.R;
import pan.alexander.calculator.domain.entities.HistoryData;

import static pan.alexander.calculator.util.Utils.spannedStringFromHtml;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewHistoryExpression;
    private final TextView textViewHistoryResult;
    private final TextView textViewHistoryDate;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewHistoryExpression = itemView.findViewById(R.id.textViewHistoryExpression);
        textViewHistoryResult = itemView.findViewById(R.id.textViewHistoryResult);
        textViewHistoryDate = itemView.findViewById(R.id.textViewHistoryDate);
    }

    public void bind(HistoryData historyData) {
        textViewHistoryExpression.setText(spannedStringFromHtml(historyData.getExpression()));
        textViewHistoryResult.setText(historyData.getResult());
        textViewHistoryDate.setText(formatTime(historyData.getTime()));
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        return dateFormat.format(date);
    }
}
