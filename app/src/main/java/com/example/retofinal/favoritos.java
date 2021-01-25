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

public class favoritos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner sp1;
    private ListView lt1;
    private ArrayList<ObjetoMunicipios> arrayMun;
    private ArrayList<ObjetoEspacios> arrayEsp;
    private ArrayList<String> nombresMun;
    private ArrayList<String> nombresEsp;
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        sp1 = findViewById(R.id.spinner);
        sp1.setOnItemSelectedListener(this);
        lt1 = findViewById(R.id.listview);
        arrayEsp = new ArrayList<ObjetoEspacios>();
        arrayMun = new ArrayList<ObjetoMunicipios>();
        nombresMun = new ArrayList<String>();
        nombresEsp = new ArrayList<String>();
        String[] opciones = {"Municipios","Espacios Naturales"};

        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        sp1.setAdapter(adapterSp);

        lt1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view,
                                    int i, long l) {
                String selected = sp1.getSelectedItem().toString();
                if(selected.equals("Municipios")){
                    datosMun(arrayMun.get(i));
                } else if(selected.equals("Espacios Naturales")){
                    datosEsp(arrayEsp.get(i));
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        arrayEsp.clear();
        arrayMun.clear();
        nombresMun.clear();
        nombresEsp.clear();
        String selected = sp1.getSelectedItem().toString();
        Log.i("selected",selected);
        conectarOnClick(selected);
        if(selected.equals("Municipios")){

            for(int x=0;x<arrayMun.size();x++){
                nombresMun.add(arrayMun.get(x).getNombre());
            }
            ArrayAdapter<String> adapterLV1 = new ArrayAdapter<String>
                    (this,android.R.layout.simple_list_item_1, nombresMun);
            lt1.setAdapter(adapterLV1);

        } else if(selected.equals("Espacios Naturales")){

            for(int x=0;x<arrayEsp.size();x++){
                nombresEsp.add(arrayEsp.get(x).getNombre());
            }
            ArrayAdapter<String> adapterLV2 = new ArrayAdapter<String>
                    (this,android.R.layout.simple_list_item_1, nombresEsp);
            lt1.setAdapter(adapterLV2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void conectarOnClick(String tipo) {
        try {

            if (isConnected()) {
                if(tipo.equals("Municipios")){
                    arrayMun = conectarMuni();
                } else if(tipo.equals("Espacios Naturales")){
                    arrayEsp = conectarEspacio();
                }

                if (null == arrayMun) { // Si la respuesta es null, una excepción ha ocurrido.
                    Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ObjetoMunicipios> conectarMuni() throws InterruptedException {

        String sql = "SELECT A.CodMuni,A.Nombre,A.Descripcion,A.CodProv FROM municipios A, favmun B WHERE A.CodMuni = B.CodMuni AND B.CodUsu="+ClientThread.codigousuario+"";
        String tipo = "municipios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayMun();
    }

    private ArrayList<ObjetoEspacios> conectarEspacio() throws InterruptedException {

        String sql = "SELECT A.CodEspacio,A.Nombre,A.Descripcion,A.Tipo FROM espacios A, favesp B WHERE A.CodEspacio = B.CodEspacio AND B.CodUsu="+ClientThread.codigousuario+"";
        String tipo = "espacios";
        ClientThread clientThread = new ClientThread(sql, tipo);

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
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    public void datosMun(ObjetoMunicipios obj){
        Intent i = new Intent(this, DatosMunicipio.class);
        i.putExtra("nombre",obj.getNombre());
        i.putExtra("descripcion",obj.getDescripcion());
        i.putExtra("codmuni",obj.getCodMuni());
        i.putExtra("ubicacion","fav");
        startActivity(i);
    }

    public void datosEsp(ObjetoEspacios obj){
        Intent i = new Intent(this, DatosEspacio.class);
        i.putExtra("nombre",obj.getNombre());
        i.putExtra("descripcion",obj.getDescripcion());
        i.putExtra("codesp",obj.getCodEspacio());
        i.putExtra("ubicacion","fav");
        startActivity(i);
    }

    public void atras(View v){
        Intent i = new Intent(this, menu_principal.class);
        startActivity(i);
    }
}