package com.example.notes.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.NotesActivity;
import com.example.notes.R;
import com.example.notes.data.DaoNotes;
import com.example.notes.data.DaoReminders;
import com.example.notes.models.Note;
import com.example.notes.models.Reminders;
import com.example.notes.ui.adapters.NotesAdapter;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private NotesViewModel notesViewModel;
    private RecyclerView rvDdata;
    private GridLayoutManager layoutManager;
    private NotesAdapter notesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        View v = inflater.inflate(R.layout.fragment_note_selector,container,false);
        rvDdata = (RecyclerView) v.findViewById(R.id.fragment_selector_RecyclerView);
        layoutManager = new GridLayoutManager(getActivity(),2);
        rvDdata.setLayoutManager(layoutManager);
        ArrayList<Note> notes = new ArrayList<>();
        notes = getNotes();
        notesAdapter = new NotesAdapter(getActivity(),notes);
        notesAdapter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NotesActivity.class);
                        intent.putExtra("id", getNotes().get(rvDdata.getChildAdapterPosition(view)).getId());
                        startActivity(intent);
                    }
                }
        );
        rvDdata.setAdapter(notesAdapter);
        return v;
    }

    private ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        DaoNotes daoNotes = new DaoNotes(getActivity().getApplicationContext());
        notes = daoNotes.getAllNotes();
        return notes;
    }
}