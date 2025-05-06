package com.example.timemarkinghr.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.controller.LocalizacaoService;
import com.example.timemarkinghr.controller.RegistroPontoController;
import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.model.Usuario;
import com.example.timemarkinghr.utils.CloudinaryService;
import com.example.timemarkinghr.utils.NetworkUtils;
import com.example.timemarkinghr.utils.SessaoManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroPontoFragment extends Fragment {

    private ImageView imgFoto;
    private TextView txtLocalizacao;
    private Button btnCapturarFoto, btnRegistrarPonto;
    private Bitmap fotoBitmap;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private RegistroPontoController registroPontoController;
    private Usuario usuario;
    private boolean localizacaoDisponivel = false;

    // Launchers para solicitação de permissões e atividades
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    fotoBitmap = (Bitmap) result.getData().getExtras().get("data");
                    imgFoto.setImageBitmap(fotoBitmap);
                    btnRegistrarPonto.setEnabled(true);
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permissão concedida - obtém localização
                    obterLocalizacaoAtual();
                } else {
                    // Permissão negada - atualiza UI
                    txtLocalizacao.setText("Localização desativada - ative nas configurações");
                    localizacaoDisponivel = false;
                    btnRegistrarPonto.setEnabled(false);
                    Toast.makeText(requireContext(),
                            "Sem permissão de localização não é possível registrar ponto",
                            Toast.LENGTH_LONG).show();
                }
            });

    public static RegistroPontoFragment newInstance() {
        return new RegistroPontoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registroPontoController = new RegistroPontoController(requireContext());
        usuario = SessaoManager.getUsuario(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_ponto, container, false);

        imgFoto = view.findViewById(R.id.imgFoto);
        txtLocalizacao = view.findViewById(R.id.txtLocalizacao);
        btnCapturarFoto = view.findViewById(R.id.btnCapturarFoto);
        btnRegistrarPonto = view.findViewById(R.id.btnRegistrarPonto);

        btnRegistrarPonto.setEnabled(false);

        btnCapturarFoto.setOnClickListener(v -> verificarPermissaoCamera());
        btnRegistrarPonto.setOnClickListener(v -> registrarPonto());

        verificarPermissaoLocalizacao();
        return view;
    }

    private void verificarPermissaoLocalizacao() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            obterLocalizacaoAtual();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    private void verificarPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            abrirCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void obterLocalizacaoAtual() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocalizacaoService localizacaoService = new LocalizacaoService(requireContext());

            localizacaoService.obterLocalizacao(
                    new LocalizacaoService.LocalizacaoCallback() {
                        @Override
                        public void onSuccess(double latitude, double longitude, String endereco) {
                            requireActivity().runOnUiThread(() -> {
                                RegistroPontoFragment.this.latitude = latitude;
                                RegistroPontoFragment.this.longitude = longitude;
                                localizacaoDisponivel = true;
                                txtLocalizacao.setText(endereco);

                                if (fotoBitmap != null) {
                                    btnRegistrarPonto.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            requireActivity().runOnUiThread(() -> {
                                txtLocalizacao.setText(errorMessage);
                                localizacaoDisponivel = false;
                                btnRegistrarPonto.setEnabled(false);

                                if (!errorMessage.contains("Permissão")) {
                                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
            );
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            txtLocalizacao.setText("Aguardando permissão de localização...");
            localizacaoDisponivel = false;
            btnRegistrarPonto.setEnabled(false);
        }
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(), "Nenhum app de câmera encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarPonto() {
        // Verificar se a foto foi tirada
        if (fotoBitmap == null) {
            Toast.makeText(requireContext(), "Tire uma foto primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se a localização está disponível
        if (!localizacaoDisponivel) {
            Toast.makeText(requireContext(), "Aguardando localização...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se a conexão com a internet está disponível
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se o usuário está logado corretamente
        if (usuario == null) {
            Toast.makeText(requireContext(), "Usuário não encontrado ou sessão expirada", Toast.LENGTH_LONG).show();
            return;
        }

        // Mostrar progresso
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Enviando foto...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Criar um ID único para a imagem
        String imagePublicId = "ponto_" + usuario.getId() + "_" + System.currentTimeMillis();

        // Fazer upload da imagem para o Cloudinary
        CloudinaryService cloudinaryService = new CloudinaryService();
        cloudinaryService.uploadImage(fotoBitmap, imagePublicId, new CloudinaryService.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();

                    // Criar o objeto de registro de ponto com a URL da imagem
                    RegistroPonto registro = new RegistroPonto(
                            usuario.getId(),
                            determinarTipoRegistro(),
                            latitude,
                            longitude,
                            imageUrl, // URL já processada pelo Cloudinary
                            android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL
                    );

                    // Chamar o método do controlador para registrar o ponto
                    registroPontoController.registrarPonto(registro, new RegistroPontoController.RegistroCallback() {
                        @Override
                        public void onSuccess() {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "Ponto registrado com sucesso!", Toast.LENGTH_SHORT).show();
                                resetarFormulario();
                            });
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "Erro: " + errorMessage, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), "Falha ao enviar foto: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    private String determinarTipoRegistro() {
        return "Entrada"; // Temporário
    }

    private String converterBitmapParaBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void resetarFormulario() {
        fotoBitmap = null;
        imgFoto.setImageResource(R.drawable.baseline_photo_camera_24);
        btnRegistrarPonto.setEnabled(false);
        localizacaoDisponivel = false;
    }
}