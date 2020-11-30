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
    public NotesAdapter() {

    }

    /**
     * Función que se ejecuta para cada elemento de la lista. Establece los datos que una nota lleva.
     * @param holder Holder al que se le agregan los datos (Proporcionado por el sistema).
     * @param position Posición de la lista (Proporcionado por el sistema).
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = listNotes.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
    }

    /**
     * Obtiene la cantidad de objetos en la lista.
     * @return Cantidad de objetos en la ista.
     */
    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    /**
     * Infla la interfaz note_selector y le asigna sus propiedades.
     * @param parent (Proporcionado por el sistema).
     * @param viewType (Proporcionado por el sistema).
     * @return El viewHolder con las opciones asignadas.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.note_selector, null);
        v.setOnClickListener(this.onClickListener);
        return new ViewHolder(v);
    }

    /**
     * Construye la clase obteniendo el contexto de la aplicación, y la lista de notas que va a manejar.
     * @param context Contexto del Activiy que lo invoca.
     * @param listNotes Lista de notas que serán agregadas.
     */
    public NotesAdapter(Context context, ArrayList<Note> listNotes) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listNotes = listNotes;
        this.context = context;
    }

    /**
     * Crea un nuevo viewHolder adaptado a las necesidades a mostrar.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final LinearLayout linearLayout;
        public TextView title;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.note_selector_cardview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.note_selector_linearlayout);
            title = (TextView) itemView.findViewById(R.id.note_selector_title);
            content = (TextView) itemView.findViewById(R.id.note_selector_content);
        }
    }
}
