package com.example.camarafotoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.camarafotoapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TOMAR_FOTO = 200;
    private static final int REQUEST_PERMISOS = 100;

    private ImageView imageView;
    private Uri fotoUri;
    private File archivoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
        imageView = findViewById(R.id.imgPreview);

        btnTomarFoto.setOnClickListener(v -> solicitarPermisos());

        Log.i("VERSION_ANDROID", "API level: " + Build.VERSION.SDK_INT);

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
                archivoFoto = crearArchivoImagen();
                fotoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", archivoFoto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, REQUEST_TOMAR_FOTO);
            } catch (IOException e) {
                Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se puede abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombreArchivo = "IMG_" + timeStamp + "_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nombreArchivo, ".jpg", directorio);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TOMAR_FOTO && resultCode == RESULT_OK) {
            imageView.setImageURI(fotoUri);
            Toast.makeText(this, "Foto guardada en: " + archivoFoto.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISOS) {
            boolean otorgado = true;

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    otorgado = false;

                    // Mostrar en pantalla qué permiso fue denegado
                    Toast.makeText(this, "Permiso NO otorgado: " + permissions[i], Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permiso otorgado: " + permissions[i], Toast.LENGTH_SHORT).show();
                }
            }

            if (otorgado) {
                tomarFoto();
            } else {
                Toast.makeText(this, "Se requieren permisos para usar la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
