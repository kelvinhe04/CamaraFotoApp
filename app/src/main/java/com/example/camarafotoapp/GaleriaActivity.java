package com.example.camarafotoapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camarafotoapp.Adapters.GaleriaAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GaleriaActivity extends AppCompatActivity {

    private RecyclerView recyclerGaleria;
    private GaleriaAdapter galeriaAdapter;
    private List<File> fotosGuardadas = new ArrayList<>();
    private Button btnBorrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        recyclerGaleria = findViewById(R.id.recyclerGaleria);

        int columnas = 3;
        recyclerGaleria.setLayoutManager(new GridLayoutManager(this, columnas));

        int espacio = 5; // píxeles de separación
        recyclerGaleria.addItemDecoration(new GridSpacingItemDecoration(espacio));

        galeriaAdapter = new GaleriaAdapter(fotosGuardadas);
        recyclerGaleria.setAdapter(galeriaAdapter);

        btnBorrar = findViewById(R.id.btnBorrarFotos);
        btnBorrar.setOnClickListener(v -> borrarTodasLasFotos());


        cargarFotos();
    }

    private void borrarTodasLasFotos() {
        File carpeta = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File f : archivos) {
                if (f.getName().startsWith("FOTO_")) {
                    f.delete();
                }
            }
            fotosGuardadas.clear();
            galeriaAdapter.notifyDataSetChanged();
        }
    }



    private void cargarFotos() {
        File carpeta = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            fotosGuardadas.clear();
            for (File f : archivos) {
                if (f.getName().startsWith("FOTO_")) {
                    fotosGuardadas.add(f);
                }
            }
            galeriaAdapter.notifyDataSetChanged();
        }
    }
}
