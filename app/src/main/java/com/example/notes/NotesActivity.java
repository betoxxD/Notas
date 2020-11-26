package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notes.data.DaoImages;
import com.example.notes.data.DaoNotes;
import com.example.notes.data.DaoReminders;
import com.example.notes.models.Image;
import com.example.notes.models.Note;
import com.example.notes.models.Reminders;
import com.example.notes.ui.ImageViewActivity;
import com.example.notes.ui.WorkManagerNotify;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NotesActivity extends AppCompatActivity {

    ArrayList<Image> images;
    ArrayList<Image> newImages;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int idImageViewOld;
    Chip chipDate;
    BottomAppBar bottomAppBar;
    private TextInputEditText tieTitle;
    private EditText etContent;
    long id;
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
    ImageView imageViewCharged;
    private TableRow tableRow;
    private TableLayout tableLayout;
    private DaoImages daoImages;
    private ImageView imageViewNew;
    private int idImageViewNew;
    private int rowCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        tableLayout = findViewById(R.id.activity_notes_linearLayout);
        tableRow = new TableRow(this);
        tableLayout.addView(tableRow);
        images = new ArrayList<>();
        newImages = new ArrayList<>();
        isReminder = false;
        daoNotes = new DaoNotes(getApplicationContext());
        daoReminders = new DaoReminders(getApplicationContext());
        daoImages = new DaoImages(getApplicationContext());
        note = new Note();
        reminder = new Reminders();
        tieTitle = findViewById(R.id.activity_notes_textinputedittext);
        etContent = findViewById(R.id.activity_notes_content);
        chipDate = findViewById(R.id.activity_notes_date_chip);

        id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
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
    protected void onDestroy() {
        super.onDestroy();
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
        if (id == -1) {
            menu.findItem(R.id.menu_bottom_notes_delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (id == -1) {
                makeImageView(currentPhotoPath,false);
            }else {
                makeImageView(currentPhotoPath, true);
            }
        }
    }

    public void showTimePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.US);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(NotesActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                btnTime.setText(dateFormatter.format(calendar.getTime()));
            }
        }, hour, minute, false);
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

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.notes.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void makeImageView(String srcImage, boolean isNew) {
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / dimensionInDp, photoH / dimensionInDp);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        if (isNew){
            imageViewNew = new ImageView(this);
            Bitmap imageBitmap = BitmapFactory.decodeFile(srcImage, bmOptions);
            float proporcion = 200 / (float) imageBitmap.getWidth();
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, (int) (imageBitmap.getHeight() * proporcion), false);
            imageViewNew.setImageBitmap(imageBitmap);
            imageViewNew.setId(idImageViewNew++);
            rowCounter++;
            imageViewNew.setPadding(5, 5, 5, 5);
            tableRow.addView(imageViewNew);
            imageViewNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotesActivity.this, ImageViewActivity.class);
                    intent.putExtra("imagePath", newImages.get(view.getId()).getSrcImage());
                    startActivity(intent);
                }
            });
        }else {
            imageViewCharged = new ImageView(this);
            Bitmap imageBitmap = BitmapFactory.decodeFile(srcImage, bmOptions);
            float proporcion = 200 / (float) imageBitmap.getWidth();
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, (int) (imageBitmap.getHeight() * proporcion), false);
            imageViewCharged.setImageBitmap(imageBitmap);
            imageViewCharged.setId(idImageViewOld++);
            rowCounter++;
            imageViewCharged.setPadding(5, 5, 5, 5);
            tableRow.addView(imageViewCharged);
            imageViewCharged.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NotesActivity.this, ImageViewActivity.class);
                    intent.putExtra("imagePath", images.get(view.getId()).getSrcImage());
                    startActivity(intent);
                }
            });
        }
        if ((rowCounter % 5) == 0) {
            tableRow = new TableRow(this);
            tableLayout.addView(tableRow);
        }
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
                                }).setNegativeButton("Cancel", null).show();
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
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance(NotesActivity.this);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });


    }


    private void showCustomDialog() {
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
        if (isReminder) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
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
                String text = btnDate.getText() + ". " + btnTime.getText();
                chipDate.setText(text);
                isReminder = true;
                customDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != -1) {
                    deleteNotify(reminder.getId() + "");
                }
                chipDate.setText("");
                chipDate.setVisibility(View.INVISIBLE);
                isReminder = false;
                customDialog.dismiss();
            }
        });
    }

    private void addNote() {
        long idInserted;
        String title = tieTitle.getText().toString();
        String content = etContent.getText().toString();
        Intent intent = new Intent();
        if (title.isEmpty() || title == null || title.matches("^[ \n\r]+$") || content.isEmpty() || content == null || title.matches("^[ \n\r]+$")) {
            Toast.makeText(NotesActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
            deleteImages();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            if (id == -1) {
                if (isReminder) {
                    Reminders reminder;
                    reminder = new Reminders(title, content, 1, btnDate.getText() + ". " + btnTime.getText());
                    idInserted = daoReminders.insertReminder(reminder);
                    if (idInserted != -1) {
                        Toast.makeText(NotesActivity.this, "Recordatorio agregado correctamente", Toast.LENGTH_SHORT).show();
                        saveImages(idInserted);
                        saveNotify();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Note note;
                    note = new Note(title, content, 0);
                    idInserted = daoNotes.insertNote(note);
                    if (idInserted != -1) {
                        saveImages(idInserted);
                        Toast.makeText(NotesActivity.this, "Nota agregada correctamente", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(NotesActivity.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (isReminder) {
                    this.reminder.setTitle(tieTitle.getText().toString());
                    this.reminder.setContent(etContent.getText().toString());
                    this.reminder.setFinishDate(chipDate.getText().toString());
                    this.reminder.setReminder(1);
                    daoImages.insertImage(id,newImages);
                    if (daoReminders.update(this.reminder)) {
                        Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    this.note.setTitle(tieTitle.getText().toString());
                    this.note.setContent(etContent.getText().toString());
                    this.note.setReminder(0);
                    daoImages.insertImage(id,newImages);
                    if (daoNotes.update(this.note)) {
                        Toast.makeText(NotesActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotesActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * Create an object of reminder and note, and set the values for the elements in the view.
     */
    private void getReminder() {
        reminder = daoReminders.getOneById(id);
        if(reminder.isReminder() == 0){
            note = new Note();
            note.setId(reminder.getId());
            note.setTitle(reminder.getTitle());
            note.setContent(reminder.getContent());
            note.setReminder(reminder.isReminder());
        }
        images = daoImages.getAll(id);
        showImages();
        tieTitle.setText(reminder.getTitle());
        etContent.setText(reminder.getContent());
        if (reminder.isReminder() == 1) {
            isReminder = true;
            chipDate.setVisibility(View.VISIBLE);
            chipDate.setText(reminder.getFinishDate());
        }
    }

    /**
     * Delete the notificaiton.
     *
     * @param tag id of the notification to delete
     */
    private void deleteNotify(String tag) {
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(this, "Para ver si se elimina", Toast.LENGTH_SHORT).show();
    }

    /**
     * Generate a random key.
     *
     * @return Random key
     */
    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * Make a Data type to send it to the workmanager and save the notification.
     *
     * @param title          Title that the notification will have
     * @param content        Content of the notification
     * @param idNotification id to identify the notification
     * @return Data that contain the title, content and id of the notification
     */
    private Data saveData(String title, String content, long idNotification) {
        return new Data.Builder()
                .putString("Title", title)
                .putString("Content", content)
                .putLong("id", idNotification).build();
    }

    /**
     * Transform the date contained in the chip button to a Date format and create a new notify with the
     * saveNotification method.
     */
    private void saveNotify() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy. HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(chipDate.getText().toString());
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        String tag = generateKey();
        Date justNow = new Date();
        long alertTime = date.getTime() - justNow.getTime();
        Data data = saveData(tieTitle.getText().toString(), etContent.getText().toString(), id);
        WorkManagerNotify.saveNotification(alertTime, data, id + "");

    }

    String currentPhotoPath;

    /**
     * Create a file that contains the image to save
     *
     * @return The file with the image.
     * @throws IOException If the file can't be created
     */
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
        if (id == -1) {
            images.add(new Image(currentPhotoPath));
        } else {
            newImages.add(new Image(id, currentPhotoPath));
        }
        return image;
    }


    private void deleteImages() {
        for (int i = 0; i < images.size(); i++) {
            File fDelete = new File(images.get(i).getSrcImage());
            if (fDelete.exists()) {
                fDelete.delete();
            }
        }
    }

    private void saveImages(long id) {
        daoImages.insertImage(id, images);
    }

    private void showImages() {
        for (int i = 0; i < images.size(); i++) {
            makeImageView(images.get(i).getSrcImage(),false);
        }
    }
}