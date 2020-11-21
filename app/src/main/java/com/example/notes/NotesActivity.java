package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notes.data.DaoNotes;
import com.example.notes.data.DaoReminders;
import com.example.notes.models.Note;
import com.example.notes.models.Reminders;
import com.example.notes.ui.WorkManagerNotify;
import com.example.notes.ui.pikers.DatePickerFragment;
import com.example.notes.ui.pikers.TimePickerFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NotesActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Chip chipDate;
    BottomAppBar bottomAppBar;
    private TextInputEditText tieTitle;
    private EditText etContent;
    int id;
    Reminders reminder;
    Note note;
    private DaoReminders daoReminders;
    private DaoNotes daoNotes;
    boolean isReminder;
    Button btnAccept;
    Button btnCancel;
    Button btnDelete;
    Button btnDate;
    Button btnTime;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        isReminder = false;
        daoNotes = new DaoNotes(getApplicationContext());
        daoReminders = new DaoReminders(getApplicationContext());
        note = new Note();
        reminder = new Reminders();
        tieTitle = findViewById(R.id.activity_notes_textinputedittext);
        etContent = findViewById(R.id.activity_notes_content);
        chipDate = findViewById(R.id.activity_notes_date_chip);

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
        chipDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
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
        if(id == -1){
            menu.findItem(R.id.menu_bottom_notes_delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notes_navigation_Tphoto:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTimePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm",Locale.US);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NotesActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                btnTime.setText(dateFormatter.format(calendar.getTime()));
            }
        },hour,minute,false);
        timePickerDialog.show();
    }

    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy", Locale.US);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NotesActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                btnDate.setText(dateFormatter.format(calendar.getTime()));

            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
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
                        showCustomDialog();
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


    }



    private void showCustomDialog(){
        AlertDialog.Builder alertDialogDateChooser = new AlertDialog.Builder(NotesActivity.this);
        View customView = LayoutInflater.from(NotesActivity.this).inflate(R.layout.dialog_custom_layout, null);
        //Set current date to button
        btnDate = customView.findViewById(R.id.dialog_button_get_finishdate);
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
        btnDate.setText(dateFormatter.format(calendar.getTime()));
        // Finish current date
        // Set current time to button
        final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
        btnTime = customView.findViewById(R.id.dialog_button_get_finishtime);
        btnTime.setText(timeFormatter.format(calendar.getTime()));
        // Finish current time on button
        btnAccept = customView.findViewById(R.id.btnAcceptDialog);
        btnCancel = customView.findViewById(R.id.btnCancelDialog);
        btnDelete = customView.findViewById(R.id.btnDeleteDialog);
        if(isReminder){
            btnDelete.setVisibility(View.VISIBLE);
        }else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
        alertDialogDateChooser.setView(customView);
        final AlertDialog customDialog = alertDialogDateChooser.create();
        customDialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.cancel();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipDate.setVisibility(View.VISIBLE);
                chipDate.setText(btnDate.getText() + ". " + btnTime.getText());
                isReminder = true;
                customDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id != -1){
                    deleteNotify(reminder.getId() + "");
                }
                chipDate.setText("");
                chipDate.setVisibility(View.INVISIBLE);
                isReminder = false;
                customDialog.dismiss();
            }
        });
    }

    private void addNote () {
        String title = tieTitle.getText().toString();
        String content = etContent.getText().toString();
        Intent intent = new Intent();
        if (title.isEmpty() || title == null || title.matches("^[ \n\r]+$") || content.isEmpty() || content == null || title.matches("^[ \n\r]+$")) {
            Toast.makeText(NotesActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            if (id == -1) {
                if(isReminder){
                    Reminders reminder;
                    reminder = new Reminders(title, content, 1, btnDate.getText() + ". " + btnTime.getText());
                    if (daoReminders.insertReminder(reminder) != -1) {
                        Toast.makeText(NotesActivity.this, "Recordatorio agregado correctamente", Toast.LENGTH_SHORT).show();
                        saveNotify();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Note note;
                    note = new Note(title,content,0);
                    DaoNotes daoNotes = new DaoNotes(getApplicationContext());
                    if(daoNotes.insertNote(note) != -1){
                        Toast.makeText(NotesActivity.this, "Nota agregada correctamente", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }else {
                        Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                if(isReminder){
                    this.reminder.setTitle(tieTitle.getText().toString());
                    this.reminder.setContent(etContent.getText().toString());
                    this.reminder.setFinishDate(chipDate.getText().toString());
                    this.note.setReminder(1);
                    if(daoReminders.update(this.reminder)){
                        Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    this.note.setTitle(tieTitle.getText().toString());
                    this.note.setContent(etContent.getText().toString());
                    this.note.setReminder(0);
                    if(daoNotes.update(this.note)){
                        Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void getReminder(){
        reminder = daoReminders.getOneById(id);
        note = daoNotes.getOneById(id);
        tieTitle.setText(reminder.getTitle());
        etContent.setText(reminder.getContent());
        if(reminder.isReminder()==1){
            isReminder = true;
            chipDate.setVisibility(View.VISIBLE);
            chipDate.setText(reminder.getFinishDate());
        }
    }

    /**
     * Delete the notificaiton
     * @param tag
     */
    private void deleteNotify(String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(this, "Para ver si se elimina", Toast.LENGTH_SHORT).show();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data saveData (String title, String content, int idNotification){
        return new Data.Builder()
                .putString("Title",title)
                .putString("Content",content)
                .putInt("id",idNotification).build();
    }

    private void saveNotify(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy. HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(chipDate.getText().toString());
        }catch (ParseException ex){
            Log.v("Exception", ex.getLocalizedMessage());
        }
        String tag = generateKey();
        Date justNow = new Date();
        long alertTime = date.getTime() - justNow.getTime();
        Data data = saveData(tieTitle.getText().toString(), etContent.getText().toString(),id);
        WorkManagerNotify.saveNotification(alertTime,data,tag);

    }

    public  static Calendar toCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Create a file and get the path to save it
     */
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}