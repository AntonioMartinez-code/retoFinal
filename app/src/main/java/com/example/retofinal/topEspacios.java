package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class topEspacios extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner sp1;
    private ListView listview;
    private ArrayList<ObjetoEspacios> arrayEspB = new ArrayList<ObjetoEspacios>();
    private ArrayList<ObjetoEspacios> arrayEspG = new ArrayList<ObjetoEspacios>();
    private ArrayList<ObjetoEspacios> arrayEspA = new ArrayList<ObjetoEspacios>();
    private ArrayList<String> nombreEsp = new ArrayList<String>();
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_espacios);

        sp1 = findViewById(R.id.spinner3);
        sp1.setOnItemSelectedListener(this);
        listview = findViewById(R.id.listView);
        String[] opciones = {"Bizkaia", "Gipuzkoa", "Alava"};

        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        sp1.setAdapter(adapterSp);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = sp1.getSelectedItem().toString();

        if(selected.equals("Bizkaia")){

            nombreEsp.clear();
            conectarOnClick(selected);
            for(int x=0;x<arrayEspB.size();x++){
                nombreEsp.add(arrayEspB.get(x).getNombre());
            }

        }else if(selected.equals("Gipuzkoa")){

            nombreEsp.clear();
            conectarOnClick(selected);
            for(int x=0;x<arrayEspG.size();x++){

                nombreEsp.add(arrayEspG.get(x).getNombre());
            }

        }else if(selected.equals("Alava")){

            nombreEsp.clear();
            conectarOnClick(selected);
            for(int x=0;x<arrayEspA.size();x++){
                nombreEsp.add(arrayEspA.get(x).getNombre());
            }

        }

        ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, nombreEsp);

        listview.setAdapter(adapterLV);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view,
                                    int i, long l) {
                if(selected.equals("Bizkaia")){
                    datos(arrayEspB.get(i));
                } else if(selected.equals("Gipuzkoa")){
                    datos(arrayEspG.get(i));
                } else if(selected.equals("Alava")){
                    datos(arrayEspA.get(i));
                }

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void conectarOnClick(String tipo) {
        try {

            if (isConnected()) {

                if(tipo.equals("Bizkaia")){
                    arrayEspB.clear();
                    arrayEspB = conectarB();
                } else if(tipo.equals("Gipuzkoa")){
                    arrayEspG.clear();
                    arrayEspG = conectarG();
                } else if(tipo.equals("Alava")){
                    arrayEspA.clear();
                    arrayEspA = conectarA();
                }


            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ObjetoEspacios> conectarB() throws InterruptedException {
        String sql = "SELECT DISTINCT A.CodEspacio, A.Nombre, A.Descripcion, A.Tipo, C.CodProv FROM espacios A,ubicaciones B, municipios C, estaciones E, datos D WHERE A.CodEspacio = B.CodEspacio AND C.CodMuniAuto = B.CodMuniAuto AND C.CodMuniAuto = E.CodMuniAuto AND E.CodEst = D.CodEst AND C.CodProv = 48 ORDER BY D.NOXgm3 DESC LIMIT 5";
        String tipo = "espacios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayEsp();
    }

    private ArrayList<ObjetoEspacios> conectarG() throws InterruptedException {
        String sql = "SELECT DISTINCT A.CodEspacio, A.Nombre, A.Descripcion, A.Tipo, C.CodProv FROM espacios A,ubicaciones B, municipios C, estaciones E, datos D WHERE A.CodEspacio = B.CodEspacio AND C.CodMuniAuto = B.CodMuniAuto AND C.CodMuniAuto = E.CodMuniAuto AND E.CodEst = D.CodEst AND C.CodProv = 20 ORDER BY D.NOXgm3 DESC LIMIT 5";
        String tipo = "espacios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayEsp();
    }

    private ArrayList<ObjetoEspacios> conectarA() throws InterruptedException {
        String sql = "SELECT DISTINCT A.CodEspacio, A.Nombre, A.Descripcion, A.Tipo, C.CodProv FROM espacios A,ubicaciones B, municipios C, estaciones E, datos D WHERE A.CodEspacio = B.CodEspacio AND C.CodMuniAuto = B.CodMuniAuto AND C.CodMuniAuto = E.CodMuniAuto AND E.CodEst = D.CodEst AND C.CodProv = 1 ORDER BY D.NOXgm3 DESC LIMIT 5";
        String tipo = "espacios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayEsp();
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

    public void atras(View v){
        Intent i = new Intent(this, menu_principal.class);
        startActivity(i);
    }

    public void datos(ObjetoEspacios obj){
        Intent i = new Intent(this, DatosEspacio.class);
        i.putExtra("nombre",obj.getNombre());
        i.putExtra("descripcion",obj.getDescripcion());
        i.putExtra("codesp",obj.getCodEspacio());
        i.putExtra("ubicacion","top");
        startActivity(i);
    }
}