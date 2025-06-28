package com.example.camarafotoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TOMAR_FOTO = 200;
    private static final int REQUEST_PERMISOS = 100;
    private ImageView imageView;
    private Uri fotoUri;
    private File archivoFoto;
    private Button btnGuardarFoto, btnTomarFoto, btnVerGaleria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.incializarControles();

        btnGuardarFoto.setEnabled(false);

        btnTomarFoto.setOnClickListener(v -> solicitarPermisos());

        btnGuardarFoto.setOnClickListener(v -> {
            if (fotoUri != null && archivoFoto != null) {
                guardarImagenDefinitivamente(archivoFoto);
                btnGuardarFoto.setEnabled(false);
            }
        });

        btnVerGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GaleriaActivity.class);
            startActivity(intent);
        });
    }

    private void incializarControles() {
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnGuardarFoto = findViewById(R.id.btnGuardarFoto);
        btnVerGaleria = findViewById(R.id.btnVerGaleria);
        imageView = findViewById(R.id.imgPreview);
    }

    private void solicitarPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            tomarFoto();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISOS);
        }
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                archivoFoto = crearArchivoImagenTemporal();
                fotoUri = FileProvider.getUriForFile(this,
                        getPackageName() + ".provider", archivoFoto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, REQUEST_TOMAR_FOTO);
            } catch (IOException e) {
                Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se puede abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearArchivoImagenTemporal() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombreArchivo = "TEMP_" + timeStamp + "_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nombreArchivo, ".jpg", directorio);
    }

    private void guardarImagenDefinitivamente(File tempFoto) {
        File destino = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "FOTO_" + System.currentTimeMillis() + ".jpg");

        try {
            copiarArchivo(tempFoto, destino);
            // Limpiar el ImageView
            imageView.setImageDrawable(null);

            // Eliminar el archivo temporal
            if (tempFoto.exists()) {
                tempFoto.delete();
            }

            Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void copiarArchivo(File origen, File destino) throws IOException {
        try (java.io.InputStream in = new java.io.FileInputStream(origen);
             java.io.OutputStream out = new java.io.FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TOMAR_FOTO && resultCode == RESULT_OK) {
            imageView.setImageURI(fotoUri);
            btnGuardarFoto.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                Toast.makeText(this, "Se requieren permisos para usar la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
