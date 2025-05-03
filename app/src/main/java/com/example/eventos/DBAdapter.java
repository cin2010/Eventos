package com.example.eventos;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Date;

public class DBAdapter  {

    Context ctx;

    private static final String TAG = "DBAdapter";
    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;
    private boolean EnviarLog=true;
    private static final  String DATABASE_NAME="eventos.db";
    private static final  int DATABASE_VERSION= 1;
    private final Context adapterContext;
    private static String DB_PATH ;


    public DBAdapter(Context context){
        this.adapterContext = context;
        this.ctx = context;

        this.DB_PATH= this.ctx.getDatabasePath(DATABASE_NAME).getAbsolutePath();

    }

    public void IniciarTransaccion() {
        mDb.beginTransaction();
    }

    public void exitoTransaccion() {
        mDb.setTransactionSuccessful();
    }

    public void terminarTransaccion() {
        mDb.endTransaction();
    }

    public Cursor ejecutaConsultaSQL(String consultaSql)
    {
        if (getEnviarLog())
            Log.d( "ejecutaConsultaSQL", consultaSql );

        Cursor cursor=mDb.rawQuery(consultaSql, new String[]{});

        if (getEnviarLog())
            Log.d( "ejecutaConsultaSQL", "Resultados "+cursor.getCount() );

        return cursor;
    }

    public Cursor ejecutaConsultaSQL(String consultaSql, String[] paramSql)
    {
        if (getEnviarLog()) {
            Log.d( "P ejecutaConsultaSQL", consultaSql );
            Integer i=0;
            for (String p: paramSql) {
                if (p==null) {
                    Log.d( "parametro"+i, "NULL" );
                } else {
                    if (p.equals(""))
                        Log.d( "parametro"+i, "CADENA VACIA" );
                    else
                        Log.d( "parametro"+i, p );
                }
                i++;
            }
        }

        Cursor cursor=mDb.rawQuery(consultaSql, paramSql);

        if (getEnviarLog())
            Log.d( "ejecutaConsultaSQL", "Resultados "+cursor.getCount() );

        return cursor;

    }


    public void ejecutaOrdenSQL(String ordenSql)
    {
        if (getEnviarLog())
            Log.d( "ejecutaOrdenSQL", ordenSql );

        mDb.execSQL(ordenSql, new String[]{});

        if (getEnviarLog()) {
            Cursor cursor=mDb.rawQuery("SELECT changes()", new String[]{});
            if (cursor.moveToFirst()) {
                Log.d( "Afectada ", cursor.getString(0));
            }
            cursor.close();
        }
    }

    public void vaciarTabla(String Tabla)
    {
        String command;
        command = "DELETE FROM "+Tabla;
        mDb.execSQL(command, new String[]{});
    }

    public void close() {
        mDbHelper.close();
    }

    public boolean getEnviarLog() {
        return EnviarLog;
    }

    public void setEnviarLog(boolean enviarLog) {
        EnviarLog = enviarLog;
    }





    public DBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(adapterContext);

        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Imposible crear la base de datos");
        }

        try {
            mDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        return this;
    }










    private static class DatabaseHelper extends SQLiteOpenHelper {

        Context helperContext;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion>oldVersion)
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("Error al copiar la base de datos");
                }
            Log.w(TAG, "Actualizando la base de datos");
            onCreate(db);
        }


        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (dbExist) {
            } else {
                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("Error al copiar la base de datos");
                }
            }
        }

        public SQLiteDatabase getDatabase() {
            String myPath = DB_PATH + DATABASE_NAME;
            return SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        private boolean checkDataBase() {
            SQLiteDatabase checkDB = null;
            try {
                String myPath = DB_PATH + DATABASE_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
            } catch (SQLiteException e) {
            }
            if (checkDB != null) {
                checkDB.close();
            }
            return checkDB != null ? true : false;
        }

        private void copyDataBase() throws IOException {

            // Abre la base de datos de la carpeta assets coomo un inputstream
            InputStream myInput = helperContext.getAssets().open(DATABASE_NAME);

            // La ruta de la nueva base de datos dentro de los datos del proyecto
            String outFileName = DB_PATH + DATABASE_NAME;

            // Abre el archivo de destino
            OutputStream myOutput = new FileOutputStream(outFileName);

            // Transfiere los bytes desde inputfile a outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Cierra los streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }

        public void deleteDataBase() throws IOException {
            // La ruta de la nueva base de datos dentro de los datos del proyecto
            String databaseFileName = DB_PATH + DATABASE_NAME;

            File myFile = new File(databaseFileName);
            if(myFile.exists())
                myFile.delete();
        }

        public void openDataBase() throws SQLException {

            // Abre la base de datos
            if (mDb==null) {
                String myPath = DB_PATH + DATABASE_NAME;
                mDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
        }

        @Override
        public synchronized void close() {

            // Cerrar
            if (mDb != null) {
                mDb.close();
                mDb=null;
            }

            super.close();

        }
    }
}
