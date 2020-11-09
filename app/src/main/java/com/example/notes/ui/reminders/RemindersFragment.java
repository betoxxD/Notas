package com.example.notes.ui.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.NotesActivity;
import com.example.notes.R;
import com.example.notes.data.DaoReminders;
import com.example.notes.models.Reminders;
import com.example.notes.ui.adapters.RemindersAdapter;
import com.example.notes.ui.notes.NotesViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class RemindersFragment extends Fragment {

    private RemindersViewModel remindersViewModel;
    private RecyclerView rvDdata;
    private GridLayoutManager layoutManager;
    private RemindersAdapter remindersAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public DaoReminders getDaoReminders(){
        return new DaoReminders(getActivity().getApplicationContext());
    }

    private ArrayList<Reminders> getReminders (){
        ArrayList<Reminders> reminders = new ArrayList<>();
        DaoReminders daoReminders = new DaoReminders(getActivity().getApplicationContext());
        reminders = daoReminders.getAll();
        return reminders;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel =
                ViewModelProviders.of(this).get(RemindersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reminders, container, false);
        View v = inflater.inflate(R.layout.fragment_reminder_selector,container,false);
        rvDdata = (RecyclerView) v.findViewById(R.id.fragment_selector_RecyclerView);
        layoutManager = new GridLayoutManager(getActivity(),2);
        rvDdata.setLayoutManager(layoutManager);
        ArrayList<Reminders> reminders = new ArrayList<>();
        reminders = getReminders();
        remindersAdapter = new RemindersAdapter(getActivity(),reminders);
        remindersAdapter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NotesActivity.class);
                        intent.putExtra("id", getReminders().get(rvDdata.getChildAdapterPosition(view)).getId());
                        startActivity(intent);
                    }
                }
        );
        rvDdata.setAdapter(remindersAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}