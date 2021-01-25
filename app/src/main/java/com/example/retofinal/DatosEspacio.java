package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DatosEspacio extends AppCompatActivity {

    private TextView tNombre,tDescripcion;
    private Button btnUbicacion,btnCamara,btnAtras;
    private String nom,desc;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_espacio);

        tNombre= findViewById(R.id.tNombre);
        tDescripcion= findViewById(R.id.tDescripcion);
        btnAtras = findViewById(R.id.btnAtras);
        btnCamara = findViewById(R.id.btnCamara);
        btnUbicacion = findViewById(R.id.btnUbicacion);

        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();

        tNombre.setText(nom);
        tDescripcion.setText(desc);
    }

    public void atras(View v){
        Intent i = new Intent(this, espacios.class);
        startActivity(i);
    }

    private void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}