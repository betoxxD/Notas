package com.example.notes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.notes.R;
import com.example.notes.ui.pikers.TimePickerFragment;
import com.google.android.material.bottomappbar.BottomAppBar;

public class NotesActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomAppBar = findViewById(R.id.bottomAppBarNotes);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_notes);
        bottomAppBarDefinition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_add_notes, menu);
        return true;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void bottomAppBarDefinition(){

    }
}