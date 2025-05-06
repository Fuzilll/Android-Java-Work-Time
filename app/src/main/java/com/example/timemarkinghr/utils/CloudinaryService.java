package com.example.timemarkinghr.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryService {
    private static final String TAG = "CloudinaryService";
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        // Configuração do Cloudinary
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "drievajuq");
        config.put("api_key", "579725112231392");
        config.put("api_secret", "_U1Y6-WQl2W2JKB5N4yRMlyYaw4");

        this.cloudinary = new Cloudinary(config);
    }

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onFailure(String errorMessage);
    }

    public void uploadImage(Bitmap bitmap, String publicId, UploadCallback callback) {
        new Thread(() -> {
            try {
                // Converter Bitmap para byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Fazer upload para o Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader()
                        .upload(imageBytes, ObjectUtils.asMap(
                                "public_id", publicId,
                                "folder", "time_marking_hr"
                        ));

                String imageUrl = (String) uploadResult.get("secure_url");
                callback.onSuccess(imageUrl);
            } catch (IOException e) {
                Log.e(TAG, "Erro ao fazer upload para o Cloudinary", e);
                callback.onFailure(e.getMessage());
            }
        }).start();
    }
}