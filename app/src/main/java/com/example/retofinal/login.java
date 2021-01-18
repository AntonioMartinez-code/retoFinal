package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {
    EditText etUsuario,etContrasena;
    TextView tvOlvidarContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        tvOlvidarContrasena = (TextView) findViewById(R.id.tvOlvidaste);

        overridePendingTransition(R.xml.slide_up,R.xml.slide_off);

        tvOlvidarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioVentana();

            }
        });
    }

    public void cambioVentana(){
        Intent i = new Intent(this, olvidarContra.class);
        startActivity(i);
    }
    public void logear(View v) throws NoSuchAlgorithmException {
        String usuario = etUsuario.getText().toString();
        String contrasena = etContrasena.getText().toString();
        SharedPreferences prefe =getSharedPreferences("base", Context.MODE_PRIVATE);

        String paraHash = contrasena;
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte dataBytes[] = paraHash.getBytes();
        md.update(dataBytes);
        byte resumen[] = md.digest();
        String hash = new String(resumen);

        String contrasenaP = prefe.getString("contrasena", "");
        String usuarioP = prefe.getString("usuario", "");

        Log.i("Contrasena hash",hash);
        Log.i("ContrasenaP",contrasenaP);


        if (!usuario.equals("") && !contrasena.equals("")) {
            if (hash.equals(contrasenaP) && usuario.equals(usuarioP) ) {
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