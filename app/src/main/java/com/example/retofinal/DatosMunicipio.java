package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;

public class DatosMunicipio extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView tNombre, tDescripcion;
    private Button btnUbicacion, btnCamara, btnAtras;
    private int CodUsu, CodMuni;
    private String nom, desc, imagenHash, ubicacion, existe;
    private ImageView imagen1;
    private CheckBox cbFav;
    private ConnectivityManager connectivityManager = null;
    ArrayList<ObjetoMunicipios> variable = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_municipio);

        tNombre = findViewById(R.id.tNombre);
        tDescripcion = findViewById(R.id.tDescripcion);
        btnAtras = findViewById(R.id.btnAtras);
        btnCamara = findViewById(R.id.btnCamara);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        imagen1 = findViewById(R.id.imageView);
        cbFav = findViewById(R.id.checkBox);
        cbFav.setOnCheckedChangeListener(this);
        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();
        CodMuni = (Integer) getIntent().getExtras().get("codmuni");
        CodUsu = ClientThread.codigousuario;
        ubicacion = getIntent().getExtras().get("ubicacion").toString();

        tNombre.setText(nom);
        tDescripcion.setText(desc);

        conectarOnClick("comprobar");
        if (!existe.equals("0")) {
            cbFav.setChecked(true);
        } else {
            cbFav.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (cbFav.isChecked()) {
            conectarOnClick("insertar");
        } else if (!cbFav.isChecked()) {
            conectarOnClick("borrar");
        }
    }

    public void atras(View v) {
        if (ubicacion.equals("lista")) {
            Intent i = new Intent(this, municipios.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, favoritos.class);
            startActivity(i);
        }
    }

    public void tomarFoto(View v) {
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File foto = new File(getExternalFilesDir(null), "foto.jpg");
        startActivityForResult(intento1, REQUEST_IMAGE_CAPTURE);
    }

    public void googleMaps(View view) throws InterruptedException {
        String sql = "SELECT latitud,longitud FROM municipios WHERE  CodMuni=" + CodMuni + "";
        String tipo = "ubicacionMun";
        ClientThread clientThread = new ClientThread(sql, tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        while (variable == null) {
           variable = clientThread.getArrayMun();
        }

    if (variable.get(0).getLatitud() == null || variable.get(0).getLongitud() == null  ){
        Toast.makeText(this, "no se puede mostrar la ubicacion", Toast.LENGTH_LONG).show();
    }else {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + variable.get(0).getLatitud() + "," + variable.get(0).getLongitud() + ""));
    startActivity(intent);
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle ex = data.getExtras();
            Bitmap imageBit = (Bitmap) ex.get("data");
            imagen1.setImageBitmap(imageBit);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imageBit.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] arrayFoto = out.toByteArray();
            imagenHash = String.valueOf(Base64.encode(arrayFoto, Base64.DEFAULT));
            conectarOnClick("");
        }
    }

    public void conectarOnClick(String tipo) {
        try {

            if (isConnected()) {
                if (tipo.equals("")) {
                    conectar();
                } else if (tipo.equals("comprobar")) {
                    existe = conectarComp();
                } else {
                    Log.i("FFF", tipo);
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
        String sql = "SELECT Count(*) FROM favmun WHERE CodUsu=" + CodUsu + " AND CodMuni=" + CodMuni + "";
        String tipo = "comprobarFav";
        ClientThread clientThread = new ClientThread(sql, tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        String variable = null;
        while (variable == null) {
            variable = clientThread.getResponse();
        }
        return clientThread.getResponse();
    }

    private void conectar() throws InterruptedException {
        String sql = "INSERT INTO fotosmun (CodUsu,CodMuni,Foto) VALUES (" + CodUsu + "," + CodMuni + ",'" + imagenHash + "')";
        String tipo = "foto";
        ClientThread clientThread = new ClientThread(sql, tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    private void conectarFav(String tip) throws InterruptedException {
        String sql = "";
        if (tip.equals("insertar")) {
            sql = "INSERT INTO favmun (CodUsu,CodMuni) VALUES (" + CodUsu + "," + CodMuni + ")";
        } else if (tip.equals("borrar")) {
            sql = "DELETE FROM favmun WHERE CodUsu=" + CodUsu + " AND CodMuni=" + CodMuni + "";
        }
        String tipo = "favorito";
        ClientThread clientThread = new ClientThread(sql, tipo);
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