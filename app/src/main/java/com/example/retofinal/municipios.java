package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class municipios extends AppCompatActivity {
    Button btnConsulta;
    TextView txview;
    private Spinner spinner;
    private TextView tv1, tv2;
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipios);
        overridePendingTransition(R.xml.slide_up, R.xml.slide_off);
        txview = (TextView) findViewById(R.id.tvNombre);
        btnConsulta = (Button) findViewById(R.id.btnConsulta);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView)  findViewById(R.id.tv2);
        spinner = (Spinner) findViewById(R.id.spinner1);
        String[] opciones = {"vizcaya", "alava", "gipuzkoa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapter);


    }
    public void buscar(View v){
        String selec = spinner.getSelectedItem().toString();
        if (selec.equals("vizcaya")) {
            tv1.setText("Bilbao");
            tv2.setText("Balmaseda");
        } else if (selec.equals("alava")) {

            tv1.setText("Victoria");
            tv2.setText("zuya");
        } else if (selec.equals("gipuzkoa")) {

            tv1.setText("San sebastian");
            tv2.setText("eibar");
        }
    }
        /*  btnConsulta.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
                Consulta();



            }
        });

*/
/*public Connection conecta(){
        Connection cnn=null;
        try{
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
           StrictMode.setThreadPolicy(politica);
            //Class.forName("com.mysql.jdbc.Driver");
           // String url = "jdbc:mysql://192.168.1.39:3306/retofinal?serverTimezone=UTC";
           // cnn = DriverManager.getConnection(url, "root", "");// Consulta sencilla en este caso.
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
           cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.7.223;databaseName=retofinal;","root","");
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cnn;
    }
    public void Consulta(){
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conecta().prepareStatement("SELECT Nombre FROM municipios WHERE CodProv= '1'");
            rs = st.executeQuery();
            //Statement stm = conecta().createStatement();
           // ResultSet rs = stm.executeQuery("SELECT nombre FROM municipios WHERE CodProv= '1'");
            if (rs.next()){
                txview.setText(rs.getString(2));
            }
        } catch (SQLException throwables) {
            Toast.makeText(this, "Error en la consulta", Toast.LENGTH_LONG).show();
        }


    }
    */

    public void conectarOnClick(View v) {
        try {
            if (isConnected()) {
                String sRespuesta = conectar();
                if (null == sRespuesta) { // Si la respuesta es null, una excepción ha ocurrido.
                    Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION", Toast.LENGTH_SHORT).show();
                } else {
                    txview.setText(sRespuesta); // Mostramos en el textView el nombre.
                }
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private String conectar() throws InterruptedException {
        String sql = "SELECT Nombre FROM municipios WHERE CodMuni=0";
        String tipo = "municipios";
        ClientThread clientThread = new ClientThread(sql,tipo);

        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
       String variable = null;
        // Esperar respusta del servidor...
        while (variable == null) {
            variable = clientThread.getResponse();
        }
        Log.i("log : metodo Conectar ", variable);
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
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }


}