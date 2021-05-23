package pan.alexander.notes.presentation.activities;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import pan.alexander.notes.R;

public class ActionModeCallback implements ActionMode.Callback {

    private ActionModeFinishedListener listener;

    public ActionModeCallback(ActionModeFinishedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate (R.menu.top_menu_item_selected, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.topMenuUnselect) {
            mode.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (listener != null) {
            listener.onActionModeFinished();
        }
        listener = null;
    }

    public interface ActionModeFinishedListener {
        void onActionModeFinished();
    }
}
