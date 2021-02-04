package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class topMunicipios extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner sp1;
    private ListView listview;
    private ArrayList<ObjetoMunicipios> arrayMunB = new ArrayList<ObjetoMunicipios>();
    private ArrayList<ObjetoMunicipios> arrayMunG = new ArrayList<ObjetoMunicipios>();
    private ArrayList<ObjetoMunicipios> arrayMunA = new ArrayList<ObjetoMunicipios>();
    private ArrayList<String> nombreMun = new ArrayList<String>();
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_municipios);

        sp1 = findViewById(R.id.spinner2);
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

            nombreMun.clear();
            conectarOnClick(selected);
            for(int x=0;x<arrayMunB.size();x++){
                nombreMun.add(arrayMunB.get(x).getNombre());
            }

        }else if(selected.equals("Gipuzkoa")){

            nombreMun.clear();
            conectarOnClick(selected);
            for(int x=0;x<arrayMunG.size();x++){

                    nombreMun.add(arrayMunG.get(x).getNombre());
            }

        }else if(selected.equals("Alava")){

            nombreMun.clear();
            conectarOnClick(selected);
            for(int x=0;x<arrayMunA.size();x++){
                    nombreMun.add(arrayMunA.get(x).getNombre());
            }

        }

        ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, nombreMun);

        listview.setAdapter(adapterLV);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view,
                                    int i, long l) {
                if(selected.equals("Bizkaia")){
                    datos(arrayMunB.get(i));
                } else if(selected.equals("Gipuzkoa")){
                    datos(arrayMunG.get(i));
                } else if(selected.equals("Alava")){
                    datos(arrayMunA.get(i));
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
                    arrayMunB.clear();
                    arrayMunB = conectarB();
                } else if(tipo.equals("Gipuzkoa")){
                    arrayMunG.clear();
                    arrayMunG = conectarG();
                } else if(tipo.equals("Alava")){
                    arrayMunA.clear();
                    arrayMunA = conectarA();
                }


            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ObjetoMunicipios> conectarB() throws InterruptedException {
        String sql = "SELECT DISTINCT m.CodMuni,m.CodMuniAuto,m.Nombre,m.Descripcion,m.CodProv FROM municipios m, estaciones e, datos d WHERE m.CodMuniAuto = e.CodMuniAuto AND e.CodEst = d.CodEst AND m.CodProv = 48 ORDER BY d.NOXgm3 DESC LIMIT 5";
        String tipo = "municipios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayMun();
    }

    private ArrayList<ObjetoMunicipios> conectarG() throws InterruptedException {
        String sql = "SELECT DISTINCT m.CodMuni,m.CodMuniAuto,m.Nombre,m.Descripcion,m.CodProv FROM municipios m, estaciones e, datos d WHERE m.CodMuniAuto = e.CodMuniAuto AND e.CodEst = d.CodEst AND m.CodProv = 20 ORDER BY d.NOXgm3 DESC LIMIT 5";
        String tipo = "municipios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayMun();
    }

    private ArrayList<ObjetoMunicipios> conectarA() throws InterruptedException {
        String sql = "SELECT DISTINCT m.CodMuni,m.CodMuniAuto,m.Nombre,m.Descripcion,m.CodProv FROM municipios m, estaciones e, datos d WHERE m.CodMuniAuto = e.CodMuniAuto AND e.CodEst = d.CodEst AND m.CodProv = 1 ORDER BY d.NOXgm3 DESC LIMIT 5";
        String tipo = "municipios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayMun();
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

    public void datos(ObjetoMunicipios obj){
        Intent i = new Intent(this, DatosMunicipio.class);
        i.putExtra("nombre",obj.getNombre());
        i.putExtra("descripcion",obj.getDescripcion());
        i.putExtra("codmuni",obj.getCodMuni());
        i.putExtra("codmuniauto",obj.getCodMuniAuto());
        i.putExtra("ubicacion","top");
        startActivity(i);
    }
}