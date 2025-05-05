package com.example.timemarkinghr.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalizacaoService {
    private static final String TAG = "LocalizacaoService";
    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private final Geocoder geocoder;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public LocalizacaoService(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public interface LocalizacaoCallback {
        void onSuccess(double latitude, double longitude, String endereco);
        void onFailure(String errorMessage);
    }

    public void obterLocalizacao(LocalizacaoCallback callback) {
        // Verificar permissão antes de continuar
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            callback.onFailure("Permissão de localização não concedida");
            return;
        }

        try {
            LocationRequest locationRequest = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 10000)
                    .setMinUpdateIntervalMillis(5000)
                    .build();

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    fusedLocationClient.removeLocationUpdates(this);
                    if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                        Location location = locationResult.getLastLocation();
                        processarLocalizacao(location, callback);
                    } else {
                        callback.onFailure("Localização não disponível");
                    }
                }
            };

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        } catch (SecurityException e) {
            Log.e(TAG, "Erro de segurança ao acessar localização", e);
            callback.onFailure("Erro de permissão ao acessar localização");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter localização", e);
            callback.onFailure("Erro ao obter localização: " + e.getMessage());
        }
    }

    private void processarLocalizacao(Location location, LocalizacaoCallback callback) {
        executor.execute(() -> {
            try {
                String endereco = obterEndereco(location.getLatitude(), location.getLongitude());
                new android.os.Handler(Looper.getMainLooper()).post(() ->
                        callback.onSuccess(location.getLatitude(), location.getLongitude(), endereco)
                );
            } catch (Exception e) {
                new android.os.Handler(Looper.getMainLooper()).post(() ->
                        callback.onFailure("Erro ao obter endereço: " + e.getMessage())
                );
            }
        });
    }

    private String obterEndereco(double latitude, double longitude) throws IOException {
        List<Address> enderecos = geocoder.getFromLocation(latitude, longitude, 1);
        if (!enderecos.isEmpty()) {
            return enderecos.get(0).getAddressLine(0);
        }
        return "Endereço não encontrado";
    }
}