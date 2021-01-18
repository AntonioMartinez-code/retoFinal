package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class registro extends AppCompatActivity {
    EditText etUsuario, etContrasena, etRepite;

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
                SharedPreferences preferencias = getSharedPreferences("base", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("usuario", usuario);


                String paraHash = contrasena;
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte dataBytes[] = paraHash.getBytes();
                md.update(dataBytes);
                byte resumen[] = md.digest();
                String hash = new String(resumen);

                editor.putString("contrasena", hash);
                    editor.commit();
                    finish();
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




}