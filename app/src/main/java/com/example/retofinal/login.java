package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    EditText etUsuario,etContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
    }

    public void logear(View v){
        String usuario = etUsuario.getText().toString();
        String contrasena = etContrasena.getText().toString();
        SharedPreferences prefe =getSharedPreferences("base", Context.MODE_PRIVATE);


        String contrasenaP = prefe.getString("contrasena", "");
        String usuarioP = prefe.getString("usuario", "");
        if (!usuario.equals("") && !contrasena.equals("")) {
            if (contrasena.equals(contrasenaP) && usuario.equals(usuarioP) ) {
                Toast.makeText(this, "login correcto", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, menu_principal.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "error al logear", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "debe rellenar los campos", Toast.LENGTH_LONG).show();
        }

    }
    public void VRegistro(View view) {
        Intent i = new Intent(this, registro.class);
        startActivity(i);
    }

}