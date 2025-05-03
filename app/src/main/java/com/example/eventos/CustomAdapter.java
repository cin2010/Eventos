package com.example.eventos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private Activity activity;
    private ArrayList id_evento, nombre, direccion, fecha;


    CustomAdapter( Context context, ArrayList id_evento, ArrayList nombre, ArrayList direccion,
                  ArrayList fecha){
        this.activity = activity;
        this.context = context;
        this.id_evento = id_evento;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fecha = fecha;
    }





    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {
        holder.id_evento.setText(String.valueOf(id_evento.get(position)));
        holder.nombre.setText(String.valueOf(nombre.get(position)));
        holder.direccion.setText(String.valueOf(direccion.get(position)));
        holder.fecha.setText(String.valueOf(fecha.get(position)));
        holder.list_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModificarActivity.class );
                intent.putExtra("id_evento",String.valueOf(id_evento.get(position)));
                intent.putExtra("nombre",String.valueOf(nombre.get(position)));
                intent.putExtra("direccion",String.valueOf(direccion.get(position)));
                intent.putExtra("fecha",String.valueOf(fecha.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id_evento.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id_evento, nombre, direccion, fecha;
        //layaut donde esta el molde del recycleview
        LinearLayout list_eventos;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id_evento = itemView.findViewById(R.id.eventi_id_txt);
            nombre = itemView.findViewById(R.id.nombre_txt);
            direccion = itemView.findViewById(R.id.direcion_txt);
            fecha = itemView.findViewById(R.id.fecha_txt);
            list_eventos = itemView.findViewById(R.id.listaeventos);
        }
    }
}
