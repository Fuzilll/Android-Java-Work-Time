package com.example.timemarkinghr.service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocalizacaoService {
    private Context context;

    public LocalizacaoService(Context context) {
        this.context = context;
    }

    public String obterLocalizacao() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "Permissão negada";
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            return obterEndereco(location.getLatitude(), location.getLongitude());
        }
        return "Localização não disponível";
    }

    private String obterEndereco(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> enderecos = geocoder.getFromLocation(latitude, longitude, 1);
            if (!enderecos.isEmpty()) {
                return enderecos.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Endereço não encontrado";
    }
}
