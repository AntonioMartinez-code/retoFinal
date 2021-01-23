package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class registro extends AppCompatActivity {
    EditText etUsuario, etContrasena, etRepite;
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        etUsuario = (EditText) findViewById(R.id.etUsuarioR);
        etContrasena = (EditText) findViewById(R.id.etContrasenaR);
        etRepite = (EditText) findViewById(R.id.etRepite);
        overridePendingTransition(R.xml.slide_up,R.xml.slide_off);
    }
    public void VRegistrar(View v) throws NoSuchAlgorithmException {
        String usuario = etUsuario.getText().toString();
        String contrasena = etContrasena.getText().toString();
        String repite = etRepite.getText().toString();
        String clave = "123";

        if (!usuario.equals("") && !contrasena.equals("") && !repite.equals("")) {
            if (contrasena.equals(repite)) {

                /*String paraHash = contrasena;
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte dataBytes[] = paraHash.getBytes();
                md.update(dataBytes);
                byte resumen[] = md.digest();*/
                //String hash = Hash.Hashear(contrasena);
                String hash = contrasena;
                try {
                    if (isConnected()) {
                        conectar(hash);
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {// This cannot happen!
                    Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
                }
                /*editor.putString("contrasena", hash);
                    editor.commit();
                    finish();*/
                    Toast.makeText(this, "Usuario registrado con exito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, login.class);
                    startActivity(intent);


            } else {

                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "hay campos vacios debe rellenarlos", Toast.LENGTH_LONG).show();
        }
    }
    public void VCancelar(View view) {
        Intent i = new Intent(this, login.class);
        startActivity(i);
    }

    private void conectar(String hash) throws InterruptedException {
        String sql = "INSERT INTO `usuarios`(`Nombre`, `Password`) VALUES ('"+ etUsuario.getText() +"','"+ hash +"')";
        String tipo = "registrar";
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
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

}