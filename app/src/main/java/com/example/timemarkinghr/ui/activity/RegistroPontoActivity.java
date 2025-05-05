package com.example.timemarkinghr.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.timemarkinghr.R;

import com.example.timemarkinghr.controller.LocalizacaoService;
import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;
import com.example.timemarkinghr.utils.NetworkUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroPontoActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private ImageView imgFoto;
    private TextView txtLocalizacao;
    private Button btnCapturarFoto, btnRegistrarPonto;
    private Bitmap fotoBitmap;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private ApiService apiService;
    private LocalizacaoService localizacaoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ponto);

        imgFoto = findViewById(R.id.imgFoto);
        txtLocalizacao = findViewById(R.id.txtLocalizacao);
        btnCapturarFoto = findViewById(R.id.btnCapturarFoto);
        btnRegistrarPonto = findViewById(R.id.btnRegistrarPonto);

        apiService = RemoteRepository.getApiService();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        localizacaoService = new LocalizacaoService(this);

        btnCapturarFoto.setOnClickListener(v -> abrirCamera());
        btnRegistrarPonto.setOnClickListener(v -> registrarPonto());

        solicitarPermissaoLocalizacao();
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            fotoBitmap = (Bitmap) data.getExtras().get("data");
            imgFoto.setImageBitmap(fotoBitmap);
        }
    }

    private void solicitarPermissaoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            obterLocalizacao();
        }
    }

    private void obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    txtLocalizacao.setText(localizacaoService.obterLocalizacao());
                } else {
                    txtLocalizacao.setText("Localização: Não disponível");
                }
            }).addOnFailureListener(e -> txtLocalizacao.setText("Erro ao obter localização"));
        }
    }

    private void registrarPonto() {
        if (fotoBitmap == null) {
            Toast.makeText(this, "Tire uma foto primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String fotoBase64 = converterBitmapParaBase64(fotoBitmap);
        RegistroPonto registro = new RegistroPonto("1", latitude, longitude, fotoBase64);

//        apiService.registrarPonto(registro).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(RegistroPontoActivity.this, "Ponto registrado com sucesso!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Log detalhado da resposta
//                    try {
//                        String errorBody = response.errorBody().string();
//                        Toast.makeText(RegistroPontoActivity.this, "Erro: " + errorBody, Toast.LENGTH_LONG).show();
//                    } catch (Exception e) {
//                        Toast.makeText(RegistroPontoActivity.this, "Erro desconhecido!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(RegistroPontoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private String converterBitmapParaBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
