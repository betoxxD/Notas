package com.example.notes.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.NotesActivity;
import com.example.notes.R;
import com.example.notes.data.DaoNotes;
import com.example.notes.models.Note;
import com.example.notes.ui.adapters.NotesAdapter;
import com.example.notes.ui.reminders.SelectorRemindersFragment;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private NotesViewModel notesViewModel;
    private RecyclerView rvDdata;
    private GridLayoutManager layoutManager;
    private NotesAdapter notesAdapter;
    private SelectorNotesFragment selectorFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectorFragment = new SelectorNotesFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contenedor_pequenio_notes, selectorFragment).commit();
        if ( getActivity().findViewById(R.id.contenedor_pequenio_notes) != null && getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_pequenio_reminders) == null ){
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contenedor_pequenio_notes, selectorFragment).commit();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportFragmentManager().beginTransaction().detach(selectorFragment).attach(selectorFragment).commit();
    }
}