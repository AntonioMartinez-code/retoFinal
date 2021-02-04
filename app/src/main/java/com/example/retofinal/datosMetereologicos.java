package com.example.retofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class datosMetereologicos extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private String fecha, nom, desc, ubicacion;
    private ConnectivityManager connectivityManager = null;
    private int CodUsu, CodMuniAuto,CodMuni;
    private Spinner spinner, spinnerH;
    private CalendarView calendar;
    private TextView tvDatos;
    private ArrayList<String> array = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_metereologicos);

        spinner = findViewById(R.id.spinner4);
        spinnerH = findViewById(R.id.spinner5);
        calendar = findViewById(R.id.calendarView2);
        calendar.setOnDateChangeListener(this);
        tvDatos = findViewById(R.id.textView10);


        CodUsu = (Integer) getIntent().getExtras().get("codusu");
        CodMuni = (Integer) getIntent().getExtras().get("codmun");
        CodMuniAuto = (Integer) getIntent().getExtras().get("codmuniauto");
        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();
        ubicacion = getIntent().getExtras().get("ubicacion").toString();

        String[] opciones = new String[5];
        for (int i=0;i<DatosMunicipio.arrayEstaciones.size();i++){
            opciones[i] = DatosMunicipio.arrayEstaciones.get(i);
        }

        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSp);

        String[] horas = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayAdapter<String> adapterSp1 = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, horas);
        spinnerH.setAdapter(adapterSp1);

    }

    private ArrayList<String> conectar() throws InterruptedException {
        Log.i("fecha",String.valueOf(calendar.getDate()));
        String sql = "SELECT COmgm3, NOgm3, NO2, PM10, ICAESTACION FROM Datos WHERE Fecha = "+fecha+" AND Hora = "+spinnerH.getSelectedItem().toString()+" AND CodEst = (SELECT CodEst FROM Estaciones WHERE Nombre ='"+spinner.getSelectedItem().toString()+"')";
        String tipo = "datos";
        ClientThread clientThread = new ClientThread(sql,tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayString();
    }

    public void conectarOnClick() {
        try {

            if (isConnected()) {
                array = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.ErrorComunicacion, Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    public void buscar(View v){
        conectarOnClick();
        Log.i("datos",array.get(0));
        //tvDatos.setText("COmgm3: "+array.get(0)+",NOgm3: "+array.get(1)+",NO2: "+array.get(2)+" PM10: "+array.get(3)+",ICAESTACION:"+array.get(4)+"");

    }

    public void atras(View v){

        Intent i = new Intent(this, DatosMunicipio.class);
        i.putExtra("nombre",nom);
        i.putExtra("descripcion",desc);
        i.putExtra("codmuniauto",CodMuniAuto);
        i.putExtra("ubicacion",ubicacion);
        i.putExtra("codmuni",CodMuni);
        startActivity(i);

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        fecha = year +"-"+ month +"-"+ dayOfMonth;
    }
}