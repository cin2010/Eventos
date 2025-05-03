package com.example.eventos;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ModificarActivity extends AppCompatActivity {


    TextView id_eventos, nombre, direccion, fecha;
    RadioButton es_favorito;
    Button modificar;
    DBAdapter dba;

    String sSentenciaSQL;

    int id_evento;
    int registro;
    String nombre_d, direccion_d, fecha_d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        //id_eventos = findViewById(R.id.mod_txtView_Ev);
        nombre = findViewById(R.id.mod_txtView_Ev);
        direccion = findViewById(R.id.mod_txtView_Direccion);
        fecha = findViewById(R.id.mod_txtView_Fecha);
        es_favorito = findViewById(R.id.mod_radioButton_Favorito);
        modificar = findViewById(R.id.btn_modificar);
        dba = new DBAdapter(ModificarActivity.this);

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean estado = es_favorito.isChecked();
                int edo = estado? 1 : 0;
                if(registro == 0){

                    dba.open();
                        // Inserta un nuevo  con los valores más comunes.
                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sSentenciaSQL = "INSERT INTO favorito " +
                                "(id_evento, es_favorito)"+
                                " VALUES " +
                                "(" +
                                "'" + String.valueOf(id_evento) + "'," +
                                "'" + String.valueOf( edo) +"'"+
                                ")";
                        dba.ejecutaOrdenSQL(sSentenciaSQL);
                    dba.close();
                }else{

                    dba.open();

                        sSentenciaSQL = "update favorito set es_favorito=" + String.valueOf(estado) + "" +
                                " where id_evento=" + String.valueOf(id_evento) + "  ";
                    dba.ejecutaOrdenSQL(sSentenciaSQL);
                    dba.close();

                }

            }
        });


        obtenDatosIntetn();
        //consultar si ese evento es favorito
        eventofav();
    }


    private  void eventofav(){

        String id= String.valueOf(id_evento);
        dba.open();
        sSentenciaSQL = "select * from favorito f" +
                " where id_evento =?";
        Cursor cursor= dba.ejecutaConsultaSQL(sSentenciaSQL, new String[]{id});
        registro=cursor.getCount();

        String act;



        if(cursor.getCount() == 0){

            es_favorito.setChecked(false);
        }else{
            while (cursor.moveToNext()) {
                //act=cursor.getString(2);
                es_favorito.setChecked(cursor.getString(2).equals("1"));

            }
        }


        cursor.close();
        dba.close();
    }

    void obtenDatosIntetn(){
        if(getIntent().hasExtra("id_evento")  && getIntent().hasExtra("nombre") &&
                getIntent().hasExtra("direccion") && getIntent().hasExtra("fecha")  ){
            //obteniendo detalles del evento seleccionado
            id_evento =   Integer.parseInt( getIntent().getStringExtra("id_evento")   );
            nombre_d = getIntent().getStringExtra("nombre");
            direccion_d = getIntent().getStringExtra("direccion");
            fecha_d = getIntent().getStringExtra("fecha");

            //pintar los datos obtenidos en los elementos de la actividad
            nombre.setText(nombre_d);
            direccion.setText(direccion_d);
            fecha.setText(fecha_d);

        }else{
            Toast.makeText(ModificarActivity.this, "Sin datos", Toast.LENGTH_SHORT).show();
        }
    }
}