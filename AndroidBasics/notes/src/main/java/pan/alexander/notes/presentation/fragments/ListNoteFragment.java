package pan.alexander.notes.presentation.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pan.alexander.notes.R;
import pan.alexander.notes.presentation.viewmodel.ListNoteViewModel;

public class ListNoteFragment extends Fragment {

    private ListNoteViewModel mViewModel;

    public static ListNoteFragment newInstance() {
        return new ListNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_note_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListNoteViewModel.class);
        // TODO: Use the ViewModel
    }

}
