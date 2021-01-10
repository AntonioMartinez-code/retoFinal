package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu_principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        overridePendingTransition(R.xml.slide_up,R.xml.slide_off);
    }
    public void VMunicipios(View view) {
        Intent i = new Intent(this, municipios.class);
        startActivity(i);
    }

    public void VEspacios(View view) {
        Intent i = new Intent(this, espacios.class);
        startActivity(i);
    }

    public void VTopMunicipios(View view) {
        Intent i = new Intent(this, topMunicipios.class);
        startActivity(i);
    }

    public void VTopEspacios(View view) {
        Intent i = new Intent(this, topEspacios.class);
        startActivity(i);
    }
    public void VFavoritos(View view) {
        Intent i = new Intent(this, favoritos.class);
        startActivity(i);
    }
    public void VAtras(View view) {
        Intent i = new Intent(this, login.class);
        startActivity(i);
    }
}