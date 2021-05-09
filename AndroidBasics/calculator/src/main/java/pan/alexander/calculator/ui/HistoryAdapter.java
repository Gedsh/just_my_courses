package pan.alexander.calculator.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pan.alexander.calculator.R;
import pan.alexander.calculator.domain.entities.HistoryData;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private List<HistoryData> historyDataList = new ArrayList<>();

    public void refreshHistory(List<HistoryData> historyDataList) {
        this.historyDataList = historyDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(historyDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyDataList.size();
    }
}
