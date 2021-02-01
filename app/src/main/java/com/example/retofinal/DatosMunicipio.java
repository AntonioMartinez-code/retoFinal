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
import java.io.IOException;
import java.util.ArrayList;

public class DatosMunicipio extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_IMAGE_CAPTURE =1;
    private TextView tNombre,tDescripcion;
    private Button btnUbicacion,btnCamara,btnAtras;
    private int CodUsu,CodMuniAuto,CodMuni;
    private String nom,desc,imagenHash,ubicacion,existe,rutaImagen,cambio;
    private ImageView imagen1;
    private CheckBox cbFav;
    private ConnectivityManager connectivityManager = null;
    public static File foto;
    private Bitmap bit=null;
    private ArrayList<ObjetoMunicipios> variable=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_municipio);

        tNombre= findViewById(R.id.tNombre);
        tDescripcion= findViewById(R.id.tDescripcion);
        btnAtras = findViewById(R.id.btnAtras);
        btnCamara = findViewById(R.id.btnCamara);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        imagen1 = findViewById(R.id.imageView);
        cbFav = findViewById(R.id.checkBox);
        cbFav.setOnCheckedChangeListener(this);
        nom = getIntent().getExtras().get("nombre").toString();
        desc = getIntent().getExtras().get("descripcion").toString();
        CodMuniAuto = (Integer)getIntent().getExtras().get("codmuniauto");
        CodMuni = (Integer)getIntent().getExtras().get("codmuni");
        CodUsu = ClientThread.codigousuario;
        ubicacion=getIntent().getExtras().get("ubicacion").toString();

        tNombre.setText(nom);
        tDescripcion.setText(desc);

        conectarOnClick("comprobar");
        if(!existe.equals("0")){
            cambio="no";
            cbFav.setChecked(true);
        }else {
            cbFav.setChecked(false);
        }
        conectarOnClick("foto");
        if(bit != null){
            btnCamara.setEnabled(false);
            imagen1.setImageBitmap(bit);
        } else{
            btnCamara.setEnabled(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(cbFav.isChecked()){
            if(cambio!="no"){
                conectarOnClick("insertar");
            }
        } else if(!cbFav.isChecked()){
            conectarOnClick("borrar");
        }
    }
    public void CompartirTexto(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, tDescripcion.getText());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }
    public void compartirFoto(View v) {
        try{
            if (rutaImagen == null) {
                Toast.makeText(getApplicationContext(), "no hay foto", Toast.LENGTH_SHORT).show();
            } else {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, rutaImagen);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, null));
            }
        }catch (Exception ex ){
            Toast.makeText(getApplicationContext(), "error al compartir foto", Toast.LENGTH_SHORT).show();

        }


    }
    public void atras(View v){
        if(ubicacion.equals("lista")){
            Intent i = new Intent(this, municipios.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this, favoritos.class);
            startActivity(i);
        }
    }

    public void tomarFoto(View v){
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File imagenArchivo = null;

        try{
            imagenArchivo = crearImagen();
        } catch (IOException e) {
            Log.e("error",e.toString());
        }

        if(imagenArchivo!=null){
            Uri fotoUri= FileProvider.getUriForFile(this,"com.example.retofinal.fileprovider",imagenArchivo);
            intento1.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
            startActivityForResult(intento1, REQUEST_IMAGE_CAPTURE);
        }


    }

    public File crearImagen() throws IOException {
        String nombreFoto ="foto_";
        File directorio = getExternalFilesDir(null);
        foto = File.createTempFile(nombreFoto,".jpg",directorio);
        rutaImagen = foto.getAbsolutePath();

        return foto;
    }
    public void googleMaps(View view) throws InterruptedException {
        String sql = "SELECT latitud,longitud FROM municipios WHERE  CodMuniAuto=" + CodMuniAuto + "";
        String tipo = "ubicacionMun";
        ClientThread clientThread = new ClientThread(sql, tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        while (variable == null) {
            variable = clientThread.getArrayMun();
        }

        if (variable.get(0).getLatitud().equals("") || variable.get(0).getLongitud().equals("")){
            Toast.makeText(this, "no se puede mostrar la ubicacion", Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + variable.get(0).getLongitud() + "," + variable.get(0).getLatitud() + ""));
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            //Bundle ex = data.getExtras();
            Bitmap imageBit = BitmapFactory.decodeFile(rutaImagen);
            imagen1.setImageBitmap(imageBit);

            conectarOnClick("");
        }
    }

    public void conectarOnClick(String tipo) {
        try {

            if (isConnected()) {
                if(tipo.equals("")) {
                    conectar();
                }else if(tipo.equals("comprobar")){
                        existe = conectarComp();
                }else if(tipo.equals("foto")){
                    bit = conectarFoto();
                }  else{
                    Log.i("FFF",tipo);
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
        String sql = "SELECT Count(*) FROM favmun WHERE CodUsu="+CodUsu+" AND CodMuni="+CodMuniAuto+"";
        String tipo = "comprobarFav";
        ClientThread clientThread = new ClientThread(sql,tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        String variable = null;
        while (variable == null) {
            variable = clientThread.getResponse();
        }
        return clientThread.getResponse();
    }

    private Bitmap conectarFoto() throws InterruptedException {
        String sql = "SELECT foto FROM fotomun WHERE CodUsu="+CodUsu+" AND CodMuni="+CodMuniAuto+"";
        String tipo = "comprobarFoto";
        ClientThread clientThread = new ClientThread(sql,tipo);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getImage();
    }

    private void conectar() throws InterruptedException {
        String sql = "INSERT INTO fotomun (CodUsu,CodMuni,foto) VALUES ("+CodUsu+","+CodMuniAuto+",?)";
        String tipo = "foto";
        ClientThread clientThread = new ClientThread(sql,tipo);
        clientThread.setFoto(foto);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    private void conectarFav(String tip) throws InterruptedException {
        String sql="";
        if(tip.equals("insertar")){
            sql = "INSERT INTO favmun (CodUsu,CodMuni) VALUES ("+CodUsu+","+CodMuniAuto+")";
        }else if(tip.equals("borrar")){
            sql = "DELETE FROM favmun WHERE CodUsu="+CodUsu+" AND CodMuni="+CodMuniAuto+"";
        }
        String tipo = "favorito";
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