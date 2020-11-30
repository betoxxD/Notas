package com.example.notes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.models.Reminders;

import java.util.ArrayList;


public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ViewHolder> {

    private ArrayList<Reminders> listReminders;
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    /**
     * Se integra la función de clic.
     * @param onClickListener Función clic personalizada.
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Constructor vacío, necesario en un adaptador.
     */
    public RemindersAdapter() {

    }

    /**
     * Función que se ejecuta para cada elemento de la lista. Establece los datos que una nota lleva.
     * @param holder Holder al que se le agregan los datos (Proporcionado por el sistema).
     * @param position Posición de la lista (Proporcionado por el sistema).
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminders reminder = listReminders.get(position);
        holder.title.setText(reminder.getTitle());
        holder.content.setText(reminder.getContent());
        holder.date.setText(reminder.getFinishDate());
    }

    /**
     * Obtiene la cantidad de objetos en la lista.
     * @return Cantidad de objetos en la ista.
     */
    @Override
    public int getItemCount() {
        return listReminders.size();
    }

    /**
     * Infla la interfaz reminder_selector y le asigna sus propiedades.
     * @param parent (Proporcionado por el sistema).
     * @param viewType (Proporcionado por el sistema).
     * @return El viewHolder con las opciones asignadas.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.reminder_selector, null);
        v.setOnClickListener(this.onClickListener);
        return new ViewHolder(v);
    }

    /**
     * Construye la clase obteniendo el contexto de la aplicación, y la lista de recordatorios que va a manejar.
     * @param context Contexto del Activiy que lo invoca.
     * @param listNotes Lista de notas que serán agregadas.
     */
    public RemindersAdapter(Context context, ArrayList<Reminders> listReminders) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listReminders = listReminders;
        this.context = context;
    }

    /**
     * Crea un nuevo viewHolder adaptado a las necesidades a mostrar.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ConstraintLayout constraintLayout;
        public TextView title;
        public TextView content;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.reminder_selector_cardview);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.reminder_selector_constraintlayout);
            title = (TextView) itemView.findViewById(R.id.reminder_selector_title);
            content = (TextView) itemView.findViewById(R.id.reminder_selector_content);
            date = itemView.findViewById(R.id.reminder_selector_date);
            date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_access_alarm_14, 0, 0, 0);

        }
    }
}
