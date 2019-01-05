package dsa.eetac.upc.edu.appgame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {

    static API createAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        String BASE_URL = "http://localhost:8080/swagger/#/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(API.class);
    }

    @POST("user/login/{userName}")
    Call<Response> login(@Path("userName") String userName, String password);

    @PUT("/register/{userName}")
    Call<Response> register(@Path("userName") String userName, String password);

    @PATCH("/banned/{userName}")
    Call<Response> banned(@Path("userName") String userName);

    @PATCH("/admin/{userName}")
    Call<Response> admin(@Path("userName") String userName);



}
