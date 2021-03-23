package com.example.aznotes;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/* Clase que se encarga de administrar una vista de renglón */
public class NotaViewHolder extends RecyclerView.ViewHolder{
    /* Variables que hacen referencia a row.xml */
    protected TextView id_nota, titulo, texto, fecha;

    public NotaViewHolder(@NonNull View itemView) {
        super(itemView);
        /* Se hace referencia a row.xml y sus TextViews */
        id_nota = itemView.findViewById(R.id.id_nota);
        titulo = itemView.findViewById(R.id.titulo_nota);
        texto = itemView.findViewById(R.id.texto_nota);
        fecha = itemView.findViewById(R.id.fecha_nota);
    }

}