package com.example.aznotes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Hashtable;

public class NotasAdapter  extends RecyclerView.Adapter<NotaViewHolder>{
    private Hashtable<String, Nota> HT_Notas;
    private View.OnClickListener listener;

    public NotasAdapter(Hashtable<String, Nota> HT_Notas, View.OnClickListener listener)
    {
        this.HT_Notas = HT_Notas;
        this.listener = listener;
    }
    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.notarow, parent, false);
        v.setOnClickListener(listener);
        NotaViewHolder NotaRow = new NotaViewHolder(v);
        return NotaRow;
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        String pos = String.valueOf(position);
        int p = HT_Notas.get(pos).getId_Nota() + 1;
        holder.id_nota.setText("Nota " + p + " -");
        holder.titulo.setText("Título: " + HT_Notas.get(pos).getTitle());
        /*int len = HT_Notas.get(pos).getTexto().length();
        if(len >= 5){
            String texto_small = HT_Notas.get(pos).getTexto().substring(0,10);
            holder.texto.setText(texto_small + " ...");
        }else{
            holder.texto.setText(HT_Notas.get(pos).getTexto());
        }*/
        String fecha[] = HT_Notas.get(pos).getFecha().split(" ");
        holder.fecha.setText("- Fecha: " + fecha[0]);
    }

    @Override
    public int getItemCount() {
        return HT_Notas.size();
    }

}