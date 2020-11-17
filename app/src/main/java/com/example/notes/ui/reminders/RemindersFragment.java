package com.example.notes.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.notes.R;

public class RemindersFragment extends Fragment {

    private RemindersViewModel remindersViewModel;

    SelectorRemindersFragment selectorFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start Reminders Fragment initialization
        selectorFragment = new SelectorRemindersFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contenedor_pequenio_reminders, selectorFragment).commit();
        if ( getActivity().findViewById(R.id.contenedor_pequenio_reminders) != null && getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor_pequenio_reminders) == null ){
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contenedor_pequenio_reminders, selectorFragment).commit();
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel =
                ViewModelProviders.of(this).get(RemindersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reminders, container, false);





        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportFragmentManager().beginTransaction().detach(selectorFragment).attach(selectorFragment).commit();
    }
}