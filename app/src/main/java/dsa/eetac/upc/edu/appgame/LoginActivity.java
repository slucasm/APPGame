package dsa.eetac.upc.edu.appgame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import dsa.eetac.upc.edu.appgame.models.BodyUser;
import dsa.eetac.upc.edu.appgame.models.Game;
import dsa.eetac.upc.edu.appgame.models.Respuesta;
import dsa.eetac.upc.edu.appgame.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";

    private API api;

    private Button loginButton;
    private Button registerButton;
    private EditText usernameInputText;
    private EditText passwordInputText;

    private String userName;
    private String password;

    private Boolean isAdmin = false;
    private Boolean loginSuccessful = false;
    private Boolean registerSuccessful;

    private String messageLogin;
    private String messageRegister;

    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = API.createAPI();


        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);
        usernameInputText = findViewById(R.id.TextInputUserName);
        passwordInputText = findViewById(R.id.TextInputPassword);

        Log.i(TAG,"Get Button's and EditText");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        final SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("registered", false);
        editor.putString("userName", "");
        editor.putString("password", "");
        editor.apply();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                login(usernameInputText.getText().toString(),passwordInputText.getText().toString());
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                register(usernameInputText.getText().toString(),passwordInputText.getText().toString());
            }
        });
    }

    public void login(String userName, String password){
        //userName = usernameInputText.getText().toString();
        //password = passwordInputText.getText().toString();

        Log.i(TAG,"Click on Login Button");


        //Call<ResponseBody> responseCall = api.login("Meritxell","holahola");
        BodyUser bodyUser = new BodyUser(userName,password);
        Call<Respuesta> responseCall = api.login(bodyUser);
        //Call<Void> responseCall = api.login(userName,password);
        Log.i(TAG,"Get login for api");

        responseCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                Boolean loginIsSuccessful = false;
                Boolean isBanned = false;
                int code = respuesta.getCode();
                String  message = respuesta.getMessage();
                progressDialog.hide();
                if (code == 2){ //Successful
                    loginIsSuccessful = true;
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(code == 1){ //User is banned
                    isBanned = true;
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(code == 0){ //Password incorrect
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(code == 3){ //User is admin
                    loginIsSuccessful = true;
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                }

                if (loginIsSuccessful){
                    final SharedPreferences sharedPref =
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("registered", true);
                    editor.putString("userName", userName);
                    editor.putString("password", password);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, PreGame_Activity.class);
                    intent.putExtra("userName",userName);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),"Connection error!",Toast.LENGTH_LONG);
                toast.show();
                progressDialog.hide();
            }
        });


    }


    public void register(String userName, String password){
        Log.i(TAG,"Click on Register Button");


        BodyUser bodyUser = new BodyUser(userName,password);


        Call<Respuesta> responseCall = api.register(bodyUser);
        Log.i(TAG,"Get register for api");

        responseCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                progressDialog.hide();
                int code = respuesta.getCode();
                String message = respuesta.getMessage();

                if (code == 1){
                    login(userName,password);
                }
                else if(code == 2){
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                }


            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),"Connection error!",Toast.LENGTH_LONG);
                toast.show();
                progressDialog.hide();
            }
        });
    }

    public void loadUsers(){
        Call<List<User>> userCall = api.loadUsers();

        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int code = response.code();
               // if (response.code() == 1){
                    List<User> userList = response.body();

                    for (int i = 0; i < userList.size(); i++){
                        Log.i(TAG, userList.get(i).getUserName());
                    }
                /*else{
                    Log.e("Login", "Else");

                    progressDialog.hide();

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }*/
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Login", t.getMessage());

                progressDialog.hide();

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }


    public void getGameOfUser(){
        Call<Game> gameCall = api.getGameOfUser("Meritxell","partida5");

        gameCall.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.isSuccessful()){
                    Game game = response.body();
                    Log.i("One game from user",game.getNameGame());

                }else{
                    Log.e(TAG,Integer.toString(response.code()));
                    Log.e("Login", "Else Games");
                    progressDialog.hide();

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                Log.e("Login game", t.getMessage());

                progressDialog.hide();

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }


}
