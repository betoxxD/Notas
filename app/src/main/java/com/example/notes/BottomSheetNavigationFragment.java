package com.example.notes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.notes.ui.ImageViewActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomSheetNavigationFragment extends BottomSheetDialogFragment {

    private static NotesActivity notesActivity;
    private static boolean hasPermission;
    private static MenuItem menuVideo;

    public static BottomSheetNavigationFragment newInstance(NotesActivity notesActivity1, boolean permission) {
        Bundle args = new Bundle();
        notesActivity = notesActivity1;
        hasPermission = permission;
        BottomSheetNavigationFragment fragment = new BottomSheetNavigationFragment();
        fragment.setArguments(args);

        menuVideo = notesActivity.findViewById(R.id.notes_navigation_Tvideo);
        return fragment;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer, null);

        dialog.setContentView(contentView);

        NavigationView navigationView = contentView.findViewById(R.id.navigation_view_notes);
        if(!hasPermission) {
            navigationView.getMenu().findItem(R.id.notes_navigation_Tvideo).setVisible(false);
        }else {
            navigationView.getMenu().findItem(R.id.notes_navigation_Tvideo).setVisible(true);
        }
        //implement navigation menu item click event
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notes_navigation_Tphoto:
                        notesActivity.dispatchTakePictureIntent();
                        break;
                    case R.id.notes_navigation_Tvideo:
                        notesActivity.dispatchTakeVideoIntent();
                        break;
                    case R.id.notes_navigation_record:
                        notesActivity.openRecordActivity();
                        break;
                }
                return false;
            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    /**
     *
     */


}
