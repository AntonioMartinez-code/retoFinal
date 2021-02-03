package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class galeriaMun extends AppCompatActivity {

        static final int REQUEST_IMAGE_CAPTURE = 1;
        private ImageView imagen1, imagen2, imagen3, imagen4;
        private Button btnCamara;
        public File foto;
        private String rutaImagen =  null, nom, desc, ubicacion;
        private ConnectivityManager connectivityManager = null;
        private int CodUsu, CodMuniAuto,CodMuni;
        private ArrayList<Bitmap> bit = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_galeria_mun);

            CodUsu = (Integer) getIntent().getExtras().get("codusu");
            CodMuni = (Integer) getIntent().getExtras().get("codmun");
            CodMuniAuto = (Integer) getIntent().getExtras().get("codmuniauto");
            nom = getIntent().getExtras().get("nombre").toString();
            desc = getIntent().getExtras().get("descripcion").toString();
            ubicacion = getIntent().getExtras().get("ubicacion").toString();
            btnCamara = findViewById(R.id.button6);
            imagen1 = findViewById(R.id.imageView8);
            imagen2 = findViewById(R.id.imageView9);
            imagen3 = findViewById(R.id.imageView10);
            imagen4 = findViewById(R.id.imageView11);

            conectarOnClick("foto");
            switch (bit.size()) {
                case 1:
                    imagen1.setImageBitmap(bit.get(0));
                    break;
                case 2:
                    imagen1.setImageBitmap(bit.get(0));
                    imagen2.setImageBitmap(bit.get(1));
                    break;
                case 3:
                    imagen1.setImageBitmap(bit.get(0));
                    imagen2.setImageBitmap(bit.get(1));
                    imagen3.setImageBitmap(bit.get(2));
                    break;
                case 4:
                    imagen1.setImageBitmap(bit.get(0));
                    imagen2.setImageBitmap(bit.get(1));
                    imagen3.setImageBitmap(bit.get(2));
                    imagen4.setImageBitmap(bit.get(3));
                    btnCamara.setEnabled(false);
                    break;
            }
        }

    public void compartirFoto(View v) {
        try{
            if (rutaImagen == null) {
                Toast.makeText(getApplicationContext(), R.string.nohayfoto, Toast.LENGTH_SHORT).show();
            } else {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, rutaImagen);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "foto"));
            }
        }catch (Exception ex ){
            Toast.makeText(getApplicationContext(), R.string.errorCompartirFoto, Toast.LENGTH_SHORT).show();

        }


    }

    public void tomarFotoMun(View v) {
            Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File imagenArchivo = null;

            try {
                imagenArchivo = crearImagen();

            } catch (IOException e) {
                Log.e("error", e.toString());
            }

            if (imagenArchivo != null) {
                Uri fotoUri = FileProvider.getUriForFile(this, "com.example.retofinal.fileprovider", imagenArchivo);
                intento1.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intento1, REQUEST_IMAGE_CAPTURE);
            }


        }

        public File crearImagen() throws IOException {
            String nombreFoto = "foto_";
            File directorio = getExternalFilesDir(null);
            foto = File.createTempFile(nombreFoto, ".jpg", directorio);
            rutaImagen = foto.getAbsolutePath();

            return foto;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                //Bundle ex = data.getExtras();
                Bitmap imageBit = BitmapFactory.decodeFile(rutaImagen);
                bit.add(imageBit);
                switch(bit.size()){
                    case 1:
                        imagen1.setImageBitmap(imageBit);
                        break;
                    case 2:
                        imagen2.setImageBitmap(imageBit);
                        break;
                    case 3:
                        imagen3.setImageBitmap(imageBit);
                        break;
                    case 4:
                        imagen4.setImageBitmap(imageBit);
                        btnCamara.setEnabled(false);
                        break;
                }

                conectarOnClick("");
            }
        }

        private ArrayList<Bitmap> conectarFoto() throws InterruptedException {
            String sql = "SELECT foto FROM fotomun WHERE CodUsu=" + CodUsu + " AND CodMuni=" + CodMuniAuto + "";
            String tipo = "comprobarFoto";
            ClientThread clientThread = new ClientThread(sql, tipo);
            Thread thread = new Thread(clientThread);
            thread.start();
            thread.join();
            return clientThread.getImageArray();
        }

        private void conectar() throws InterruptedException {
            String sql = "INSERT INTO fotomun (CodUsu,CodMuni,foto) VALUES (" + CodUsu + "," + CodMuniAuto + ",?)";
            String tipo = "foto";
            ClientThread clientThread = new ClientThread(sql, tipo);
            clientThread.setFoto(foto);
            Thread thread = new Thread(clientThread);
            thread.start();
            thread.join();
        }

        public void conectarOnClick(String tipo) {
            try {

                if (isConnected()) {
                    if (tipo.equals("")) {
                        conectar();
                    } else if (tipo.equals("foto")) {
                        bit = conectarFoto();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
                }

            } catch (InterruptedException e) {// This cannot happen!
                Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), R.string.ErrorComunicacion, Toast.LENGTH_SHORT).show();
            }
            return ret;
        }

        public void atrasMunicipio(View v){

            Intent i = new Intent(this, DatosMunicipio.class);
            i.putExtra("nombre",nom);
            i.putExtra("descripcion",desc);
            i.putExtra("codmuniauto",CodMuniAuto);
            i.putExtra("ubicacion",ubicacion);
            i.putExtra("codmuni",CodMuni);
            startActivity(i);

        }
}
