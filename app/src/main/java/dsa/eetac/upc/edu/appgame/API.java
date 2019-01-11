package dsa.eetac.upc.edu.appgame;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import dsa.eetac.upc.edu.appgame.models.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {

    static final String TAG = "API";


    @POST("user/login/{userName}")
    Call<Void> login(@Path("userName") String userName, @Body String password);

    @PUT("user/register/{userName}")
    Call<Void> register(@Path("userName") String userName,@Body String password);

    @GET("user/loadUsers")
    Call<List<User>> loadUsers();

    /*@PATCH("banned/{userName}")
    Call<Response> banned(@Path("userName") String userName);

    @PATCH("admin/{userName}")
    Call<Response> admin(@Path("userName") String userName);*/

    @GET("game/llistaGames")
    Call<List<Game>> getListGames();

    @GET("game/getGame/{userName}/{nameGame}")
    Call<Game> getGameOfUser(@Path("userName") String userName,@Path("nameGame") String nameGame);

    static API createAPI() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        String BASE_URL = "http://147.83.7.203:8080/APIGame/";
        //String BASE_URL = "http://147.83.7.203:8080/swagger/#/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.i(TAG,"Connect to Api");
        return retrofit.create(API.class);
    }





}
