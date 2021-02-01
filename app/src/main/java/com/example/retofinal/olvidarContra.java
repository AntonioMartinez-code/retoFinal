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

import java.security.NoSuchAlgorithmException;

public class olvidarContra extends AppCompatActivity {
    EditText nombreUsu,contraNueva,repiteContra;
    private ConnectivityManager connectivityManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_contra);
        nombreUsu = (EditText) findViewById(R.id.etNombreUsuario);
        contraNueva = (EditText) findViewById(R.id.etContrasenaNueva);
        repiteContra = (EditText) findViewById(R.id.etRepiteContra);
    }



        public void Ok(View v) throws NoSuchAlgorithmException {
            String nombreUsuario = nombreUsu.getText().toString();
            String contrasenaNueva = contraNueva.getText().toString();
           String repiteCon = repiteContra.getText().toString();




            if (!nombreUsuario.equals("") && !contrasenaNueva.equals("") && !repiteCon.equals("")) {
                if (contrasenaNueva.equals(repiteCon)) {


                    String hash = contrasenaNueva;
                    try {
                        if (isConnected()) {
                            conectar(hash);
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {// This cannot happen!
                        Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(this, "contraseña actualizada con exito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, login.class);
                    startActivity(intent);


                } else {

                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "hay campos vacios debe rellenarlos", Toast.LENGTH_LONG).show();
            }
        }
        public void Vatras(View view) {
            Intent i = new Intent(this, login.class);
            startActivity(i);
        }

        private void conectar(String contra) throws InterruptedException {
            String sql = "UPDATE usuarios set Password = '" + contra + "' WHERE Nombre='" + nombreUsu.getText() + "'";
            String tipo = "cambioContra";
            ClientThread clientThread = new ClientThread(sql, tipo);
            try{
                Thread thread = new Thread(clientThread);
                thread.start();
                thread.join();
            }catch(Exception e){
                Toast.makeText(this, "error al cambiar contraseña", Toast.LENGTH_LONG).show();
            }

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
