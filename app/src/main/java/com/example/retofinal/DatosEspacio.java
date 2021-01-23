package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class DatosEspacio extends AppCompatActivity {

    private TextView tNombre,tDescripcion;
    private Button btnUbicacion,btnCamara,btnAtras;
    private String nom,desc,imagenHash;
    private int CodUsu,CodEspacio;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imagen1;
    private ConnectivityManager connectivityManager = null;

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
        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();
        CodEspacio = (Integer)getIntent().getExtras().get("codesp");
        CodUsu = ClientThread.codigousuario;

        tNombre.setText(nom);
        tDescripcion.setText(desc);
    }

    public void atras(View v){
        Intent i = new Intent(this, espacios.class);
        startActivity(i);
    }

    public void tomarFoto(View v) {
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File foto = new File(getExternalFilesDir(null), "foto.jpg");
        startActivityForResult(intento1, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle ex = data.getExtras();
            Bitmap imageBit = (Bitmap) ex.get("data");
            imagen1.setImageBitmap(imageBit);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imageBit.compress(Bitmap.CompressFormat.JPEG, 100,out);
            byte[] arrayFoto = out.toByteArray();
            imagenHash = String.valueOf(Base64.encode(arrayFoto, Base64.DEFAULT));
            conectarOnClick(null);
        }
    }

    public void conectarOnClick(View v) {
        try {

            if (isConnected()) {
                if (null == conectar()) { // Si la respuesta es null, una excepción ha ocurrido.
                    Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private String conectar() throws InterruptedException {
        String sql = "INSERT INTO fotosesp (CodUsu,CodEspacio,Foto) VALUES ("+CodUsu+","+CodEspacio+",'"+imagenHash+"')";
        String tipo = "foto";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return "ok";
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

}