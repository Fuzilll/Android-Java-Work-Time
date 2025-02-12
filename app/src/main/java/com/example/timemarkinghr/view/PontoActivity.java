package com.example.timemarkinghr.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.controller.RegistroPontoController;
import com.example.timemarkinghr.dao.FirestoreDAO;
import com.example.timemarkinghr.model.RegistroPonto;
import com.example.timemarkinghr.model.Usuario;
import com.example.timemarkinghr.service.LocalizacaoService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PontoActivity extends AppCompatActivity {

    // Constantes para permissões e captura de imagem
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    // Definindo os elementos da interface
    private Button btnTirarFoto, btnMarcarPonto;
    private ImageView imageView;
    private TextView tvLocation, tvTipoPonto;

    // Instâncias dos controladores e serviços
    private RegistroPontoController controller;
    private LocalizacaoService localizacaoService;
    private Bitmap imageBitmap;
    private String urlImagem;  // URL da imagem no Cloudinary

    // URL e preset do Cloudinary para upload de imagens
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/drievajuq/image/upload";
    private static final String CLOUDINARY_UPLOAD_PRESET = "time_marking_hr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializando os componentes da interface
        btnTirarFoto = findViewById(R.id.button);
        btnMarcarPonto = findViewById(R.id.btnMarcarPonto);
        imageView = findViewById(R.id.imageView);
        tvLocation = findViewById(R.id.tvLocation);
        tvTipoPonto = findViewById(R.id.tvTipoPonto);

        // Inicializando os controladores e serviços
        controller = new RegistroPontoController();
        localizacaoService = new LocalizacaoService(this);

        // Configurando o botão para tirar foto
        btnTirarFoto.setOnClickListener(v -> abrirCamera());

        // Configurando o botão para marcar ponto
        btnMarcarPonto.setOnClickListener(v -> {
            // Verificando se a foto foi tirada antes de registrar o ponto
            if (imageBitmap == null) {
                Toast.makeText(this, "Tire uma foto antes de marcar o ponto!", Toast.LENGTH_SHORT).show();
            } else {
                uploadImagemCloudinary(imageBitmap);  // Envia a imagem para o Cloudinary
            }
        });
    }

    // Função para abrir a câmera e capturar uma imagem
    private void abrirCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Inicia a captura de imagem
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Verifica o resultado da requisição de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera();  // Caso a permissão tenha sido concedida, abre a câmera
            } else {
                Toast.makeText(this, "Permissão da câmera negada!", Toast.LENGTH_SHORT).show();  // Caso a permissão seja negada
            }
        }
    }

    // Trata o resultado da captura de imagem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Recebe a imagem tirada e exibe na interface
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            Toast.makeText(this, "Foto tirada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    // Função para fazer o upload da imagem para o Cloudinary
    private void uploadImagemCloudinary(Bitmap bitmap) {
        // Converte a imagem em bitmap para um array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        OkHttpClient client = new OkHttpClient();

        // Configura o corpo da requisição para o upload da imagem
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), data))
                .addFormDataPart("upload_preset", CLOUDINARY_UPLOAD_PRESET)
                .build();

        // Cria a requisição HTTP
        Request request = new Request.Builder()
                .url(CLOUDINARY_URL)
                .post(requestBody)
                .build();

        // Envia a requisição de upload
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(PontoActivity.this, "Erro ao enviar imagem!", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Extrai a URL da imagem do Cloudinary a partir da resposta JSON
                    String jsonResponse = response.body().string();
                    urlImagem = jsonResponse.split("\"url\":\"")[1].split("\"")[0].replace("\\/", "/");

                    runOnUiThread(() -> {
                        Toast.makeText(PontoActivity.this, "Imagem enviada!", Toast.LENGTH_SHORT).show();
                        marcarPonto();  // Após o upload da imagem, marca o ponto
                    });
                }
            }
        });
    }

    // Função para registrar o ponto no sistema
    private void marcarPonto() {
        // Obtém a localização do usuário e a data/hora atual
        String localizacao = localizacaoService.obterLocalizacao();
        String dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Cria um novo registro de ponto
        Usuario usuario = new Usuario("003", "Teste2", "fer@gmail.com");
        RegistroPonto registroPonto = new RegistroPonto(usuario.getUid(), "Entrada", dataHoraAtual, localizacao, urlImagem);

        // Prepara os dados para serem enviados ao Firestore
        Map<String, Object> ponto = new HashMap<>();
        ponto.put("usuario", Map.of("uid", usuario.getUid(), "nome", usuario.getNome(), "email", usuario.getEmail()));
        ponto.put("registroPonto", Map.of("userId", registroPonto.getUserId(), "tipoPonto", registroPonto.getTipoPonto(), "dataHora", registroPonto.getDataHora(), "localizacao", registroPonto.getLocalizacao(), "imageUrl", registroPonto.getImageUrl()));

        // Envia os dados para o Firestore
        controller.registrarPonto(usuario.getUid(), ponto, new FirestoreDAO.OnFirestoreCallback() {
            @Override
            public void onSuccess() {
                // Exibe uma mensagem de sucesso ao registrar o ponto
                Toast.makeText(PontoActivity.this, "Ponto registrado com sucesso!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception e) {
                // Exibe uma mensagem de erro caso falhe ao registrar o ponto
                Toast.makeText(PontoActivity.this, "Erro ao registrar ponto", Toast.LENGTH_LONG).show();
            }
        });
    }
}
