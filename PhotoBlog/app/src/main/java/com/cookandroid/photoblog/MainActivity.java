package com.cookandroid.photoblog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int READ_MEDIA_IMAGES_PERMISSION_CODE = 1001;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    private static final String UPLOAD_URL = "https://tang0923.pythonanywhere.com/api_root/Post/";
    private static final String AUTH_TOKEN = "07b1dcc313964c234a2414dd5d371b9e7eb4005a";

    private Uri imageUri = null;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            imageUri = result.getData().getData();
                            String filePath = getRealPathFromURI(imageUri);

                            executorService.execute(() -> {
                                String uploadResult;
                                try {
                                    uploadResult = uploadImage(filePath);
                                } catch (IOException | JSONException e) {
                                    uploadResult = "Upload failed: " + e.getMessage();
                                    Log.e("PhotoBlog", "Upload failed", e);
                                }

                                String finalUploadResult = uploadResult;
                                handler.post(() ->
                                        Toast.makeText(MainActivity.this, finalUploadResult, Toast.LENGTH_LONG).show()
                                );
                            });
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            MainActivity.this,
                            Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                READ_MEDIA_IMAGES_PERMISSION_CODE
                        );
                    } else {
                        openImagePicker();
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                READ_EXTERNAL_STORAGE_PERMISSION_CODE
                        );
                    } else {
                        openImagePicker();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_MEDIA_IMAGES_PERMISSION_CODE
                || requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor == null) {
            return contentUri.toString();
        }

        try {
            if (!cursor.moveToFirst()) {
                return contentUri.toString();
            }

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            return path != null ? path : contentUri.toString();
        } finally {
            cursor.close();
        }
    }

    private String uploadImage(String imagePath) throws IOException, JSONException {
        HttpURLConnection connection = null;
        OutputStreamWriter outputStreamWriter = null;

        try {
            URL url = new URL(UPLOAD_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "JWT " + AUTH_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            String now = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ssXXX",
                    Locale.KOREA
            ).format(new Date());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("author", 1);
            jsonObject.put("title", "안드로이드-REST API 테스트");
            jsonObject.put("text", "선택한 이미지 경로: " + imagePath);
            jsonObject.put("created_date", now);
            jsonObject.put("published_date", now);
            // jsonObject.put("image", imagePath);

            outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(jsonObject.toString());
            outputStreamWriter.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK
                    || responseCode == HttpURLConnection.HTTP_CREATED) {
                Log.d("PhotoBlog", "Upload success: " + responseCode);
                return "Upload success (" + responseCode + ")";
            }

            throw new IOException("Server returned HTTP " + responseCode);
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
