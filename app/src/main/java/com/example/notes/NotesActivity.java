package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.data.DaoReminders;
import com.example.notes.models.Reminders;
import com.example.notes.ui.pikers.DatePickerFragment;
import com.example.notes.ui.pikers.TimePickerFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class NotesActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    private TextInputEditText tieTitle;
    private EditText etContent;
    int id;
    Reminders reminder;
    private DaoReminders daoReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        reminder = new Reminders();
        tieTitle = findViewById(R.id.activity_notes_textinputedittext);
        etContent = findViewById(R.id.activity_notes_content);
        id = getIntent().getIntExtra("id",-1);
        if(id != -1){
            getReminder();
        }
        bottomAppBar = findViewById(R.id.bottomAppBarNotes);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_notes);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_notes);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
                finish();
            }
        });
        bottomAppBarDefinition();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addNote();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bottom_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void bottomAppBarDefinition() {
        //find id
        bottomAppBar = findViewById(R.id.bottomAppBarNotes);

        //set bottom bar to Action bar as it is similar like Toolbar
        setSupportActionBar(bottomAppBar);

        //click event over Bottom bar menu item
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_bottom_notes_reminders:
                        Toast.makeText(NotesActivity.this, "Reminder.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_bottom_notes_delete:
                        new AlertDialog.Builder(NotesActivity.this)
                                .setTitle("Delete")
                                .setMessage("Are you sure to delete this note?")
                                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                daoReminders.delete(id);
                                Toast.makeText(NotesActivity.this, "Eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).setNegativeButton("Cancel",null).show();
                        break;
                }
                return false;
            }
        });

        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });

        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });

    }
    private void addNote () {
        String title = tieTitle.getText().toString();
        String content = etContent.getText().toString();
        Reminders reminder;
        Intent intent = new Intent();
        if (title.isEmpty() || title == null || title.matches("^[ \n\r]+$") || content.isEmpty() || content == null || title.matches("^[ \n\r]+$")) {
            Toast.makeText(NotesActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            if (id == -1) {
                reminder = new Reminders(title, content, 0, "FechaRandom");
                DaoReminders daoReminders = new DaoReminders(getApplicationContext());
                if (daoReminders.insertReminder(reminder) != -1) {
                    Toast.makeText(NotesActivity.this, "Nota agregada correctamente", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                }
            }else {
                this.reminder.setTitle(tieTitle.getText().toString());
                this.reminder.setContent(etContent.getText().toString());
                if(daoReminders.update(this.reminder)){
                    Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getReminder(){
        daoReminders = new DaoReminders(getApplicationContext());
        reminder = daoReminders.getOneById(id);
        tieTitle.setText(reminder.getTitle());
        etContent.setText(reminder.getContent());
    }
}