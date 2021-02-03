package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class topMunicipios extends AppCompatActivity {
    ListView listview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_municipios);
    }

    public void consulta(){
        String sql = "Select ";
    }
}