package dsa.eetac.upc.edu.appgame;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import dsa.eetac.upc.edu.appgame.models.BodyUser;
import dsa.eetac.upc.edu.appgame.models.Game;
import dsa.eetac.upc.edu.appgame.models.Respuesta;
import dsa.eetac.upc.edu.appgame.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    static final String TAG = "API";


    @POST("user/login/")
    Call<Respuesta> login(@Body BodyUser bodyUser);

    @POST("user/register/")
    Call<Respuesta> register(@Body BodyUser bodyUser);

    @GET("user/loadUsers")
    Call<List<User>> loadUsers();

    @GET("game/gameList/{userName}")
    Call<List<Game>> getListGamesOfUser(@Path("userName") String userName);

    @GET("game/getGame/{userName}/{nameGame}")
    Call<Game> getGameOfUser(@Path("userName") String userName,@Path("nameGame") String nameGame);

    @POST("game/newGame/{userName}/{gameName}")
    Call<Respuesta> newGame(@Path("userName") String userName, @Path("gameName") String gameName);

    @POST("user/putUserAndGame/{userName}/{gameName}")
    Call<Respuesta> putUserAndGame(@Path("userName") String userName,@Path("gameName") String gameName);

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
