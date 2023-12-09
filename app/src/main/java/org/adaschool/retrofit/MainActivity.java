package org.adaschool.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.bumptech.glide.Glide;
import org.adaschool.retrofit.databinding.ActivityMainBinding;
import org.adaschool.retrofit.network.RetrofitInstance;
import org.adaschool.retrofit.network.dto.BreedsListDto;
import org.adaschool.retrofit.network.service.DogApiService;

import java.io.IOException;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("org.adaschool.retrofit",MODE_PRIVATE);
        DogApiService dogApiService = RetrofitInstance.getRetrofitInstance(sharedPreferences).create(DogApiService.class);

        Call<BreedsListDto> call = loadDogBreeds(dogApiService);
//        try {
//            call.execute();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        loadDogInfo();
    }

    @NonNull
    private static Call<BreedsListDto> loadDogBreeds(DogApiService dogApiService) {
        Call<BreedsListDto> call = dogApiService.getAllBreeds();
        call.enqueue(new Callback<BreedsListDto>() {
            @Override
            public void onResponse(Call<BreedsListDto> call, Response<BreedsListDto> response) {
                if (response.isSuccessful()) {
                    Map<String, String[]> breeds = response.body().getMessage();
                    for (Map.Entry<String, String[]> entry : breeds.entrySet()) {
                        Log.d(TAG, "Raza: " + entry.getKey());
                        for (String subRaza : entry.getValue()) {
                            Log.d(TAG, "Subraza: " + subRaza);
                        }
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta de la API");
                }
            }

            @Override
            public void onFailure(Call<BreedsListDto> call, Throwable t) {
                Log.e(TAG, "Error al llamar a la API", t);
            }
        });
        return call;
    }

    private void loadDogInfo() {
        String dogImageUrl = "https://images.dog.ceo/breeds/retriever-chesapeake/n02099849_1830.jpg";
        String dogName = "Chesapeake Retriever";
        binding.textView.setText(dogName);
        Glide.with(this)
                .load(dogImageUrl)
                .into(binding.imageView);
    }

    private void logout() {
        sharedPreferences.edit().remove("token").apply();
    }

    private void saveToken() {
        sharedPreferences.edit().putString("token", "token").apply();
    }

}
