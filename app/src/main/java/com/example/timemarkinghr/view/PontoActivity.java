package com.example.timemarkinghr.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timemarkinghr.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PontoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Uri photoUri;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private String tipoPonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ponto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recupera o tipo de ponto enviado pela Intent
        tipoPonto = getIntent().getStringExtra("tipoPonto");
        TextView tvTipoPonto = findViewById(R.id.tvTipoPonto);
        tvTipoPonto.setText("Tipo de Ponto: " + tipoPonto);

        // Inicializa Firestore e FusedLocationProviderClient
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Referências à UI
        imageView = findViewById(R.id.imageView);
        Button btnMarcarPonto = findViewById(R.id.btnMarcarPonto);

        // Configura o botão para marcar ponto
        btnMarcarPonto.setOnClickListener(v -> getCurrentLocation());
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            TextView tvLocation = findViewById(R.id.tvLocation);
                            tvLocation.setText("Lat: " + latitude + ", Long: " + longitude);

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        } else {
                            Toast.makeText(PontoActivity.this, "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                photoUri = saveImageToStorage(imageBitmap);
                registrarPonto(photoUri, latitude, longitude, tipoPonto);
            }
        }
    }

    private Uri saveImageToStorage(Bitmap bitmap) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, "ponto_foto.jpg");
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void registrarPonto(Uri photoUri, double latitude, double longitude, String tipoPonto) {
        if (photoUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference photoRef = storageRef.child("pontos/" + System.currentTimeMillis() + ".jpg");

            photoRef.putFile(photoUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            savePontoToFirestore(imageUrl, latitude, longitude, tipoPonto);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Falha ao enviar a foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void savePontoToFirestore(String imageUrl, double latitude, double longitude, String tipoPonto) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> ponto = new HashMap<>();
        ponto.put("userId", userId);
        ponto.put("timestamp", System.currentTimeMillis());
        ponto.put("imageUrl", imageUrl);
        ponto.put("latitude", latitude);
        ponto.put("longitude", longitude);
        ponto.put("tipoPonto", tipoPonto);

        db.collection("pontos")
                .add(ponto)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Ponto registrado com sucesso!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao registrar ponto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
