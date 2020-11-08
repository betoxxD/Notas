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
import com.example.notes.models.Reminders;

import java.util.ArrayList;


public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ViewHolder> {

    private ArrayList<Reminders> listReminders;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    public void setOnClickListener (View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public RemindersAdapter() {
        // Required empty public constructor
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminders reminder = listReminders.get(position);
        holder.title.setText(reminder.getTitle());
        holder.content.setText(reminder.getContent());
    }

    @Override
    public int getItemCount() {
        return listReminders.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.reminder_selector, null);
        v.setOnClickListener(this.onClickListener);
        return new ViewHolder(v);
    }

    public RemindersAdapter(Context context, ArrayList<Reminders> listReminders){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listReminders = listReminders;
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
            cardView = (CardView) itemView.findViewById(R.id.reminder_selector_cardview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.reminder_selector_linearlayout);
            title = (TextView) itemView.findViewById(R.id.reminder_selector_title);
            content = (TextView) itemView.findViewById(R.id.reminder_selector_content);
        }
    }
}
