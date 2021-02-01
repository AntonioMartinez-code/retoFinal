package com.example.retofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThread implements Runnable {
    private String sResultado = null;
    private String sql;
    private String tipo;
    private Bitmap imagen;
    private ArrayList<Bitmap> imagenArray = new ArrayList<Bitmap>();
    private ArrayList<ObjetoMunicipios> arrayMun = new ArrayList<ObjetoMunicipios>();
    private ArrayList<ObjetoEspacios> arrayEsp = new ArrayList<ObjetoEspacios>();
    public static int codigousuario;
    File foto;

    public void setFoto(File foto) {
        this.foto = foto;
    }

    public ClientThread(String sql, String tipo) {
        this.sql = sql;
        this.tipo = tipo;
    }

    @Override
    public void run() {
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection con = null;
        String sIP;
        String sPuerto;
        String sBBDD;


        try {
            Class.forName("com.mysql.jdbc.Driver");

            //Aqui pondriamos la IP y puerto.
            sIP = "192.168.7.231";
            //sIP = "192.168.1.136";//casa
            sPuerto = "3306";
            sBBDD = "retofinal";

            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");

            switch(tipo){

                case "login":
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();//--

                    while (rs.next()) {
                        codigousuario=rs.getInt(1);
                        String var1 = rs.getString(2);
                        sResultado = var1;
                    }
                    break;
                case "registrar":
                    st = con.prepareStatement(sql);
                    st.execute(sql);
                    break;
                case "municipios":
                    arrayMun.clear();
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        ObjetoMunicipios mun = new ObjetoMunicipios();
                        mun.setCodMuni(rs.getInt(1));
                        mun.setCodMuniAuto(rs.getInt(2));
                        mun.setNombre(rs.getString(3));
                        mun.setDescripcion(rs.getString(4));
                        mun.setCodProv(rs.getInt(5));
                        arrayMun.add(mun);
                    }
                    break;
                case "espacios":
                    arrayEsp.clear();
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        ObjetoEspacios esp = new ObjetoEspacios();
                        esp.setCodEspacio(rs.getInt(1));
                        esp.setNombre(rs.getString(2));
                        esp.setDescripcion(rs.getString(3));
                        esp.setTipo(rs.getString(4));
                        try {
                            esp.setCodProv(rs.getInt(5));
                        }catch (Exception e){
                        }

                        arrayEsp.add(esp);
                    }
                    break;
                case "ubicacionMun":
                    arrayMun.clear();
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        ObjetoMunicipios mun = new ObjetoMunicipios();
                        Log.i("latitud",rs.getString(1));
                        mun.setLatitud(rs.getString(1));
                        mun.setLongitud(rs.getString(2));

                        arrayMun.add(mun);
                    }
                    break;
                case "ubicacionEsp":
                    arrayEsp.clear();
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        ObjetoEspacios esp = new ObjetoEspacios();
                        Log.i("latitud",rs.getString(1));
                        esp.setLatitud(rs.getString(1));
                        esp.setLongitud(rs.getString(2));

                        arrayEsp.add(esp);
                    }
                    break;
                case "foto":
                    FileInputStream convertir = new FileInputStream(foto);
                    st = con.prepareStatement(sql);
                    st.setBlob(1,convertir,foto.length());
                    st.executeUpdate();
                    break;
                case "favorito":
                    st = con.prepareStatement(sql);
                    st.execute(sql);
                    break;
                case "comprobarFav":
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        String var1 = rs.getString(1);
                        sResultado = var1;
                    }
                    break;
                case "comprobarFoto":
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    imagenArray.clear();
                    while (rs.next()) {
                        Blob var1 = rs.getBlob(1);
                        int blobLength = (int) var1.length();
                        byte[] blobByte = var1.getBytes(1,blobLength);
                        imagen = BitmapFactory.decodeByteArray(blobByte,0,blobByte.length);
                        imagenArray.add(imagen);
                    }
                    break;
            }

        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", "");
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("SQLException", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Exception", "");
            e.printStackTrace();
        } finally {// Intentamos cerrar _todo.
            try {
                // Cerrar ResultSet
                if (rs != null) {
                    rs.close();
                }// Cerrar PreparedStatement
                if (st != null) {
                    st.close();
                }// Cerrar Connection
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                Log.e("Exception_cerrando todo", "");
                e.printStackTrace();
            }
        }
    }

    public String getResponse() {

        return sResultado;
    }

    public Bitmap getImage() {

        return imagen;
    }

    public ArrayList<Bitmap> getImageArray() {

        return imagenArray;
    }

    public ArrayList<ObjetoMunicipios> getArrayMun() {

        return arrayMun;
    }

    public ArrayList<ObjetoEspacios> getArrayEsp() {

        return arrayEsp;
    }
}

