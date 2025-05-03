package com.example.eventos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.eventos.databinding.ActivityListaEventosBinding;

import java.util.ArrayList;

public class ListaEventosActivity extends AppCompatActivity {

String sSentenciaSQL = "";


RecyclerView vistaReciclable;
FloatingActionButton boton_agragar;
DBAdapter dba;

ArrayList<String> id_eventos, nombre, direccion, fecha, imagen;
CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_eventos);
        vistaReciclable = findViewById(R.id.recyclerview);
        boton_agragar = findViewById(R.id.floatingActionButton);
        dba = new DBAdapter(ListaEventosActivity.this);



        boton_agragar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaEventosActivity.this,NuevosEventosActivity.class);
                startActivity(intent);

            }
        });


        id_eventos = new ArrayList<>();
        nombre  = new ArrayList<>();
        direccion  = new ArrayList<>();
        fecha  = new ArrayList<>();
        imagen = new ArrayList<>();

        CargarEventos();

        customAdapter= new CustomAdapter(ListaEventosActivity.this, id_eventos, nombre, direccion, fecha);
        vistaReciclable.setAdapter(customAdapter);
        vistaReciclable.setLayoutManager(new LinearLayoutManager(ListaEventosActivity.this));
    }




    private void CargarEventos() {

        dba.open();
        sSentenciaSQL = "select * from eventos e";
        Cursor cursor = dba.ejecutaConsultaSQL(sSentenciaSQL);
        Boolean habilitados=(cursor.getCount()>0);
        if(cursor.getCount() == 0){
            //empty_imageview.setVisibility(View.VISIBLE);
            //no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id_eventos.add(cursor.getString(0));
                nombre.add(cursor.getString(1));
                direccion.add(cursor.getString(2));
                fecha.add(cursor.getString(3));
                imagen.add(cursor.getString(4));
            }
            //empty_imageview.setVisibility(View.GONE);
            //no_data.setVisibility(View.GONE);
        }


        cursor.close();
        dba.close();

    }

}