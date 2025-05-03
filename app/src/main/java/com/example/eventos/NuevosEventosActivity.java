package com.example.eventos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NuevosEventosActivity extends AppCompatActivity {

    private EditText NombreEvento;
    private EditText Direccion;
    private EditText Fecha;
    private Button Guardar;

    String mNomEvento;
    String mDireccion;
    String mFecha;

    DBAdapter dba;
    String sSentenciaSQL;


    Calendar c = Calendar.getInstance();
    Date FechaHoy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nuevos_eventos);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        //componentes de la actividad de guardar evento
        NombreEvento=(EditText) findViewById(R.id.eT_NombreEvento);
        Direccion=(EditText) findViewById(R.id.eT_Direccion);
        Fecha=(EditText) findViewById(R.id.eT_Fecha);

        Guardar = (Button) findViewById(R.id.btn_guardar) ;
        dba = new DBAdapter(NuevosEventosActivity.this);

        FechaHoy=c.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");

        Fecha.setText(dateFormat.format(FechaHoy));
        //escuchar el click del evento del boton en la actividad guardar evento
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerDatos();


                    dba.open();

                    sSentenciaSQL = "INSERT INTO eventos " +
                            "(nombre, direccion, fecha)"+
                            " VALUES " +
                            "( ' " +mNomEvento+ "'," +
                            "'" + mDireccion + "',"+
                            "'"+ mFecha +" ')";
                    dba.ejecutaOrdenSQL(sSentenciaSQL);

                Toast.makeText(NuevosEventosActivity.this, "Evento grabado correctamente", Toast.LENGTH_SHORT).show();


            }
        });


    }



    void ObtenerDatos(){




         mNomEvento = String.valueOf(NombreEvento.getText());
         mDireccion = String.valueOf(Direccion.getText());
         mFecha =String.valueOf(Fecha.getText()) ;
    }


}