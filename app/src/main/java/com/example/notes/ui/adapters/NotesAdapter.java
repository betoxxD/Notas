package com.example.notes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.models.Note;

import java.util.ArrayList;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private ArrayList<Note> listNotes;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    public void setOnClickListener (View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public NotesAdapter() {
        // Required empty public constructor
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = listNotes.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.note_selector, null);
        v.setOnClickListener(this.onClickListener);
        return new ViewHolder(v);
    }

    public NotesAdapter(Context context, ArrayList<Note> listNotes){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listNotes = listNotes;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final LinearLayout linearLayout;
        public TextView title;
        public TextView content;

        public ViewHolder(View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.note_selector_cardview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.note_selector_linearlayout);
            title = (TextView) itemView.findViewById(R.id.note_selector_title);
            content = (TextView) itemView.findViewById(R.id.note_selector_content);
        }
    }
}
