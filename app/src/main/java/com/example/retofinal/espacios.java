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

public class espacios extends AppCompatActivity {

    private Button btnBuscar,btnAtrasMun;
    private ListView listview;
    private Spinner spinner;
    private String filtro;
    private ConnectivityManager connectivityManager = null;
    private ArrayList<ObjetoEspacios> arrayEsp,arrayEspP;
    private ArrayList<String> nombreEsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espacios);
        overridePendingTransition(R.xml.slide_up, R.xml.slide_off);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnAtrasMun = findViewById(R.id.btnAtrasEsp);
        spinner = findViewById(R.id.spinner1);
        listview = findViewById(R.id.listview);

        String[] opciones = {"Bizkaia", "Gipuzkoa", "Alava"};
        filtro="";
        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapterSp);

        arrayEsp = new ArrayList<ObjetoEspacios>();
        arrayEspP = new ArrayList<ObjetoEspacios>();
        nombreEsp = new ArrayList<String>();

        conectarOnClick(null);

        for(int x=0;x<arrayEsp.size();x++){
            nombreEsp.add(arrayEsp.get(x).getNombre());
        }
        ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, nombreEsp);
        listview.setAdapter(adapterLV);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view,
                                    int i, long l) {
                if(filtro.equals("si")){
                    datos(arrayEspP.get(i));
                } else datos(arrayEsp.get(i));
            }
        });
    }
    public void buscar(View v){
        String selec = spinner.getSelectedItem().toString();
        filtro="si";
        if (selec.equals("Bizkaia")) {
            nombreEsp.clear();
            arrayEspP.clear();
            for(int x=0;x<arrayEsp.size();x++){
                if(arrayEsp.get(x).getCodProv() == 48){
                    nombreEsp.add(arrayEsp.get(x).getNombre());
                    arrayEspP.add(arrayEsp.get(x));
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreEsp);

                listview.setAdapter(adapterLV);
            }

        } else if (selec.equals("Gipuzkoa")) {
            nombreEsp.clear();
            for(int x=0;x<arrayEsp.size();x++){
                if(arrayEsp.get(x).getCodProv() == 20){
                    nombreEsp.add(arrayEsp.get(x).getNombre());
                    arrayEspP.add(arrayEsp.get(x));
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreEsp);

                listview.setAdapter(adapterLV);
            }

        } else if (selec.equals("Alava")) {
            nombreEsp.clear();
            for(int x=0;x<arrayEsp.size();x++){
                if(arrayEsp.get(x).getCodProv() == 1){
                    nombreEsp.add(arrayEsp.get(x).getNombre());
                    arrayEspP.add(arrayEsp.get(x));
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreEsp);

                listview.setAdapter(adapterLV);
            }

        }
    }

    public void conectarOnClick(View v) {
        try {

            if (isConnected()) {
                arrayEsp = conectar();
                if (null == arrayEsp) { // Si la respuesta es null, una excepción ha ocurrido.
                    Toast.makeText(getApplicationContext(), R.string.ErrorComunicacion, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ObjetoEspacios> conectar() throws InterruptedException {

        String sql = "SELECT A.CodEspacio, A.Nombre, A.Descripcion, A.Tipo, C.CodProv FROM espacios A,ubicaciones B, municipios C WHERE A.CodEspacio = B.CodEspacio AND C.CodMuniAuto = B.CodMuniAuto";
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
        i.putExtra("ubicacion","lista");
        startActivity(i);
    }
}