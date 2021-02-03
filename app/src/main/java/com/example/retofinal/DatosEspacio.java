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

public class DatosEspacio extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private TextView tNombre,tDescripcion;
    private Button btnUbicacion,btnCamara,btnAtras;
    private String nom,desc,imagenHash,ubicacion,existe,rutaImagen,cambio;
    private int CodUsu,CodEspacio;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imagen1;
    private CheckBox cbFav;
    private ConnectivityManager connectivityManager = null;
    ArrayList<ObjetoEspacios> variable = null;
    public static File foto;
    private Bitmap bit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_espacio);

        tNombre= findViewById(R.id.tNombre);
        tDescripcion= findViewById(R.id.tDescripcion);
        btnAtras = findViewById(R.id.btnAtras);
        btnCamara = findViewById(R.id.btnCamara);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        imagen1 = findViewById(R.id.imageView2);
        cbFav = findViewById(R.id.checkBox2);
        cbFav.setOnCheckedChangeListener(this);
        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();
        CodEspacio = (Integer)getIntent().getExtras().get("codesp");
        ubicacion= getIntent().getExtras().get("ubicacion").toString();
        CodUsu = ClientThread.codigousuario;

        tNombre.setText(nom);
        tDescripcion.setText(desc);

        conectarOnClick("comprobar");
        if(!existe.equals("0")){
            cambio ="no";
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

    public void tomarFoto(View v) {

        Intent i = new Intent(this, galeria.class);
        i.putExtra("codusu", CodUsu);
        i.putExtra("codesp", CodEspacio);
        i.putExtra("nombre", nom);
        i.putExtra("descripcion", desc);
        i.putExtra("ubicacion", ubicacion);
        startActivity(i);

    }

    public void googleMaps(View view) throws InterruptedException {
        String sql = "SELECT latitud,longitud FROM espacios WHERE  CodEspacio=" + CodEspacio + "";
        String tipo = "ubicacionEsp";
        ClientThread clientThread = new ClientThread(sql, tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        while (variable == null) {
            variable = clientThread.getArrayEsp();
        }

        if (variable.get(0).getLatitud() == null || variable.get(0).getLongitud() == null  ){
            Toast.makeText(this, R.string.nosepuedemostrarub, Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + variable.get(0).getLongitud() + "," + variable.get(0).getLatitud() + ""));
            startActivity(intent);
        }
    }

    public void conectarOnClick(String tipo) {
        try {

            if (isConnected()) {
                if(tipo.equals("")) {
                   // conectar();
                }else if(tipo.equals("comprobar")){
                    existe = conectarComp();
                }  else{
                    Log.i("FFF",tipo);
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
        String sql = "SELECT Count(*) FROM favesp WHERE CodUsu="+CodUsu+" AND CodEspacio="+CodEspacio+"";
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

    private void conectarFav(String tip) throws InterruptedException {
        String sql="";
        if(tip.equals("insertar")){
            sql = "INSERT INTO favesp (CodUsu,CodEspacio) VALUES ("+CodUsu+","+CodEspacio+")";
        }else if(tip.equals("borrar")){
            sql = "DELETE FROM favesp WHERE CodUsu="+CodUsu+" AND CodEspacio="+CodEspacio+"";
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
    public void CompartirTexto(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tDescripcion.getText());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    public void atras(View v){
        if(ubicacion.equals("lista")){
            Intent i = new Intent(this, espacios.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this, favoritos.class);
            startActivity(i);
        }

    }

}