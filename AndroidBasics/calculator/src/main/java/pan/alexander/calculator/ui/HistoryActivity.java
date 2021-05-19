package pan.alexander.calculator.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import pan.alexander.calculator.App;
import pan.alexander.calculator.R;
import pan.alexander.calculator.databinding.ActivityHistoryBinding;
import pan.alexander.calculator.domain.MainInteractor;
import pan.alexander.calculator.domain.entities.HistoryData;
import pan.alexander.calculator.viewmodel.HistoryViewModel;

public class HistoryActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    private ActivityHistoryBinding binding;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        initRecycler();

        initItemTouchHandler();

        observeHistoryChanges();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.historyActionBarTitle);
        }
    }

    private void initRecycler() {
        RecyclerView historyRecyclerView = binding.historyRecyclerView;
        historyAdapter = new HistoryAdapter();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void initItemTouchHandler() {
        MainInteractor mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback
                = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                List<HistoryData> historyData = historyViewModel.getHistoryLiveData().getValue();
                if (historyData != null) {
                    mainInteractor.deleteHistoryEntry(historyData.get(position))
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.historyRecyclerView);
    }

    private void observeHistoryChanges() {
        Observer<List<HistoryData>> historyDataObserver = historyData ->
                historyAdapter.refreshHistory(historyData);

        LiveData<List<HistoryData>> historyLiveData = historyViewModel.getHistoryLiveData();
        historyLiveData.observe(this, historyDataObserver);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
