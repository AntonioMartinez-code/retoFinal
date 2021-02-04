package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DatosMunicipio extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private TextView tNombre,tDescripcion;
    private Button btnUbicacion,btnCamara,btnAtras;
    private int CodUsu,CodMuniAuto,CodMuni;
    private String nom,desc,ubicacion,existe,cambio;
    private CheckBox cbFav;
    private ConnectivityManager connectivityManager = null;
    private Bitmap bit=null;
    private ArrayList<ObjetoMunicipios> variable=null;
    static ArrayList<String> arrayEstaciones = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_municipio);

        tNombre= findViewById(R.id.tNombre);
        tDescripcion= findViewById(R.id.tDescripcion);
        btnAtras = findViewById(R.id.btnAtras);
        btnCamara = findViewById(R.id.btnCamara);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        cbFav = findViewById(R.id.checkBox);
        cbFav.setOnCheckedChangeListener(this);
        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();
        CodMuniAuto = (Integer)getIntent().getExtras().get("codmuniauto");
        CodMuni = (Integer)getIntent().getExtras().get("codmuni");
        CodUsu = ClientThread.codigousuario;
        ubicacion=getIntent().getExtras().get("ubicacion").toString();

        tNombre.setText(nom);
        tDescripcion.setText(desc);

        conectarOnClick("comprobar");
        if(!existe.equals("0")){
            cambio="no";
            cbFav.setChecked(true);
        }else {
            cbFav.setChecked(false);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(cbFav.isChecked()){
            if(cambio!="no"){
                conectarOnClick("insertar");
            }
        } else if(!cbFav.isChecked()){
            conectarOnClick("borrar");
        }
    }
    public void CompartirTexto(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tDescripcion.getText());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    public void tomarFoto(View v) {

        Intent i = new Intent(this, galeriaMun.class);
        i.putExtra("codusu", CodUsu);
        i.putExtra("codmun", CodMuni);
        i.putExtra("codmuniauto", CodMuniAuto);
        i.putExtra("nombre", nom);
        i.putExtra("descripcion", desc);
        i.putExtra("ubicacion", ubicacion);
        startActivity(i);

    }

    public void googleMaps(View view) throws InterruptedException {
        String sql = "SELECT latitud,longitud FROM municipios WHERE  CodMuniAuto=" + CodMuniAuto + "";
        String tipo = "ubicacionMun";
        ClientThread clientThread = new ClientThread(sql, tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        while (variable == null) {
            variable = clientThread.getArrayMun();
        }

        if (variable.get(0).getLatitud().equals("") || variable.get(0).getLongitud().equals("")) {
            Toast.makeText(this, R.string.nosepuedemostrarub, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + variable.get(0).getLongitud() + "," + variable.get(0).getLatitud() + ""));
            startActivity(intent);
        }
    }

    public void conectarOnClick(String tipo) {
        try {

            if (isConnected()) {
                if(tipo.equals("")) {
                    //conectar();
                }else if(tipo.equals("comprobar")){
                        existe = conectarComp();
                }else if(tipo.equals("estaciones")){
                arrayEstaciones = conectarEst();
                }else{
                    conectarFav(tipo);
                }

            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }
    private String conectarComp() throws InterruptedException {
        String sql = "SELECT Count(*) FROM favmun WHERE CodUsu="+CodUsu+" AND CodMuni="+CodMuniAuto+"";
        String tipo = "comprobarFav";
        ClientThread clientThread = new ClientThread(sql,tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        String variable = null;
        while (variable == null) {
            variable = clientThread.getResponse();
        }
        return clientThread.getResponse();
    }

    private ArrayList<String> conectarEst() throws InterruptedException {
        String sql = "SELECT Nombre FROM estaciones WHERE CodMuniAuto="+CodMuniAuto+"";
        String tipo = "estaciones";
        ClientThread clientThread = new ClientThread(sql,tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getArrayString();
    }

    private void conectarFav(String tip) throws InterruptedException {
        String sql="";
        if(tip.equals("insertar")){
            sql = "INSERT INTO favmun (CodUsu,CodMuni) VALUES ("+CodUsu+","+CodMuniAuto+")";
        }else if(tip.equals("borrar")){
            sql = "DELETE FROM favmun WHERE CodUsu="+CodUsu+" AND CodMuni="+CodMuniAuto+"";
        }
        String tipo = "favorito";
        ClientThread clientThread = new ClientThread(sql,tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
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

    public void datosMetereologicos(View v){

        conectarOnClick("estaciones");
        if(arrayEstaciones.size() == 0){
            Toast.makeText(getApplicationContext(), "No hay datos metereologicos.", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(this, datosMetereologicos.class);
            i.putExtra("codusu", CodUsu);
            i.putExtra("codmun", CodMuni);
            i.putExtra("codmuniauto", CodMuniAuto);
            i.putExtra("nombre", nom);
            i.putExtra("descripcion", desc);
            i.putExtra("ubicacion", ubicacion);
            startActivity(i);
        }

    }

    public void atras(View v){
        if(ubicacion.equals("lista")){
            Intent i = new Intent(this, municipios.class);
            startActivity(i);
        }else if(ubicacion.equals("fav")){
            Intent i = new Intent(this, favoritos.class);
            startActivity(i);
        }else if(ubicacion.equals("top")){
            Intent i = new Intent(this, topMunicipios.class);
            startActivity(i);
        }
    }
}