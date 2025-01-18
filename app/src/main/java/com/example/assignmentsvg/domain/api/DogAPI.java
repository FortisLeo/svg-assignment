package com.example.assignmentsvg.domain.api;

import com.example.assignmentsvg.data.model.DogImage;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class DogAPI {

    private static final String BASE_URL = "https://dog.ceo/api/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public interface DogApiService {
        @GET("breeds/image/random")
        Call<DogImage> getRandomDogImage();
    }
}
