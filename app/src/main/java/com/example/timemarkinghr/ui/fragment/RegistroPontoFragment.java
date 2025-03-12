package com.example.timemarkinghr.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroPontoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroPontoFragment extends Fragment {

    private ImageView imgFoto;
    private TextView txtLocalizacao;
    private Button btnCapturarFoto, btnRegistrarPonto;
    private Bitmap fotoBitmap;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private ApiService apiService;
    private LocalizacaoService localizacaoService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistroPontoFragment() {
        // Required empty public constructor
    }
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    fotoBitmap = (Bitmap) result.getData().getExtras().get("data");
                    imgFoto.setImageBitmap(fotoBitmap);
                }
            });

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroPontoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroPontoFragment newInstance(String param1, String param2) {
        RegistroPontoFragment fragment = new RegistroPontoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_ponto, container, false);

        imgFoto = view.findViewById(R.id.imgFoto);
        txtLocalizacao = view.findViewById(R.id.txtLocalizacao);
        btnCapturarFoto = view.findViewById(R.id.btnCapturarFoto);
        btnRegistrarPonto = view.findViewById(R.id.btnRegistrarPonto);

        apiService = RemoteRepository.getApiService();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        localizacaoService = new LocalizacaoService(requireContext());

        btnCapturarFoto.setOnClickListener(v -> abrirCamera());
        btnRegistrarPonto.setOnClickListener(v -> registrarPonto());

        solicitarPermissaoLocalizacao();
        return view;
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        }
    }

    private void solicitarPermissaoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            obterLocalizacao();
        }
    }

    private void obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
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
            Toast.makeText(getContext(), "Tire uma foto primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(getContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String fotoBase64 = converterBitmapParaBase64(fotoBitmap);
        RegistroPonto registro = new RegistroPonto("1", latitude, longitude, fotoBase64);

        apiService.registrarPonto(registro).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ponto registrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Erro ao registrar ponto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String converterBitmapParaBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
