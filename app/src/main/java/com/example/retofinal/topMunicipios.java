package com.example.retofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class topMunicipios extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner sp1;
    private ListView listview;
    private ArrayList<ObjetoMunicipios> arrayMun;
    private ArrayList<ObjetoMunicipios> arrayMunB = new ArrayList<ObjetoMunicipios>();
    private ArrayList<ObjetoMunicipios> arrayMunG = new ArrayList<ObjetoMunicipios>();
    private ArrayList<ObjetoMunicipios> arrayMunA = new ArrayList<ObjetoMunicipios>();
    private ArrayList<String> nombreMun = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_municipios);

        sp1 = findViewById(R.id.spinner2);
        sp1.setOnItemSelectedListener(this);

        String[] opciones = {"Bizkaia", "Gipuzkoa", "Alava"};

        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_item, opciones);
        sp1.setAdapter(adapterSp);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selected = sp1.getSelectedItem().toString();

        if(selected.equals("Bizkaia")){

            nombreMun.clear();
            arrayMunB.clear();
            for(int x=0;x<arrayMun.size();x++){
                if(arrayMun.get(x).getCodProv() == 48){
                    nombreMun.add(arrayMun.get(x).getNombre());
                    arrayMunB.add(arrayMun.get(x));
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreMun);

                listview.setAdapter(adapterLV);
            }

        }else if(selected.equals("Gipuzkoa")){

            nombreMun.clear();
            arrayMunG.clear();
            for(int x=0;x<arrayMun.size();x++){
                if(arrayMun.get(x).getCodProv() == 20){
                    nombreMun.add(arrayMun.get(x).getNombre());
                    arrayMunG.add(arrayMun.get(x));
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreMun);

                listview.setAdapter(adapterLV);
            }

        }else if(selected.equals("Alava")){

            nombreMun.clear();
            arrayMunA.clear();
            for(int x=0;x<arrayMun.size();x++){
                if(arrayMun.get(x).getCodProv() == 1){
                    nombreMun.add(arrayMun.get(x).getNombre());
                    arrayMunA.add(arrayMun.get(x));
                }

                ArrayAdapter<String> adapterLV = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1, nombreMun);

                listview.setAdapter(adapterLV);
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}