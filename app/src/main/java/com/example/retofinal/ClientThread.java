package com.example.retofinal;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientThread implements Runnable {
    private String sResultado = null;
    private String sql;

    public ClientThread(String sql) {
        this.sql = sql;
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
            //sIP = "192.168.2.91";
            sIP = "192.168.7.223";
            sPuerto = "3306";
            sBBDD = "retofinal";

            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");// Consulta sencilla en este caso.
            //String sql = "SELECT Nombre FROM municipios";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();//--

            while (rs.next()) {
                String var1 = rs.getString(1);

                Log.i("XXXXXXX", var1);
                sResultado = var1;
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
}

