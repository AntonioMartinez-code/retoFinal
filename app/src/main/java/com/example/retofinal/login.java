package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private ConnectivityManager connectivityManager = null;
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
       // SharedPreferences prefe =getSharedPreferences("base", Context.MODE_PRIVATE);


        //String hash = Hash.Hashear(contrasena);
        String hash = contrasena;
        //String contrasenaP = prefe.getString("contrasena", "");
        //String usuarioP = prefe.getString("usuario", "");

        String contrasenaP="";
        try {
            if (isConnected()) {
                String sRespuesta = conectar();
                if (null == sRespuesta) { // Si la respuesta es null, una excepción ha ocurrido.
                    Toast.makeText(getApplicationContext(), R.string.ErrorComunicacion, Toast.LENGTH_SHORT).show();
                } else {

                    contrasenaP = sRespuesta;
                    if (!usuario.equals("") && !contrasena.equals("")) {
                        if (hash.equals(contrasenaP)) {
                            Toast.makeText(this, R.string.loginCorrecto, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, menu_principal.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, R.string.errorlogin, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this, R.string.rellenarcampos, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }



    }
    public void VRegistro(View view) {
        Intent i = new Intent(this, registro.class);
        startActivity(i);
    }

    private String conectar() throws InterruptedException {

        String sql = "SELECT CodUsu,Password FROM usuarios WHERE Nombre='"+ etUsuario.getText()  +"'";
        String tipo = "login";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        // Esperar respusta del servidor...

       // Log.i("log : metodo Conectar ", variable);
        return clientThread.getResponse();
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

}