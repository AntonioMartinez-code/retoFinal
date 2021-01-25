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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class municipios extends AppCompatActivity {
    private Button btnBuscar,btnAtrasMun;
    private ListView listview;
    private Spinner spinner;
    private ConnectivityManager connectivityManager = null;
    private ArrayList<ObjetoMunicipios> arrayMun;
    private ArrayList<String> nombreMun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipios);
        overridePendingTransition(R.xml.slide_up, R.xml.slide_off);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnAtrasMun = findViewById(R.id.btnAtrasEsp);
        spinner = findViewById(R.id.spinner1);
        listview = findViewById(R.id.listview);

        String[] opciones = {"Bizkaia", "Gipuzkoa", "Alava"};

        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSp);

        arrayMun = new ArrayList<ObjetoMunicipios>();
        nombreMun = new ArrayList<String>();

        conectarOnClick(null);

        for(int x=0;x<arrayMun.size();x++){
            nombreMun.add(arrayMun.get(x).getNombre());
        }
        ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, nombreMun);
        listview.setAdapter(adapterLV);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view,
                                    int i, long l) {

                datos(arrayMun.get(i));
            }
        });
    }
    public void buscar(View v){
        String selec = spinner.getSelectedItem().toString();
        if (selec.equals("Bizkaia")) {
            nombreMun.clear();

            for(int x=0;x<arrayMun.size();x++){
                if(arrayMun.get(x).getCodProv() == 1){
                    nombreMun.add(arrayMun.get(x).getNombre());
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreMun);

                listview.setAdapter(adapterLV);
            }

        } else if (selec.equals("Gipuzkoa")) {
            nombreMun.clear();
            for(int x=0;x<arrayMun.size();x++){
                if(arrayMun.get(x).getCodProv() == 2){
                    nombreMun.add(arrayMun.get(x).getNombre());
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreMun);

                listview.setAdapter(adapterLV);
            }

        } else if (selec.equals("Alava")) {
            nombreMun.clear();
            for(int x=0;x<arrayMun.size();x++){
                if(arrayMun.get(x).getCodProv() == 3){
                    nombreMun.add(arrayMun.get(x).getNombre());
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreMun);

                listview.setAdapter(adapterLV);
            }

        }
    }

    public void conectarOnClick(View v) {
        try {

            if (isConnected()) {
                arrayMun = conectar();
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

    private ArrayList<ObjetoMunicipios> conectar() throws InterruptedException {
        String sql = "SELECT CodMuni,Nombre,Descripcion,CodProv FROM municipios";
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
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
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
        i.putExtra("ubicacion","lista");
        startActivity(i);
    }
}