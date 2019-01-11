package dsa.eetac.upc.edu.appgame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import dsa.eetac.upc.edu.appgame.models.User;
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

        Toast toast = Toast.makeText(getApplicationContext(),"Hola",Toast.LENGTH_LONG);
        toast.show();
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

        getListGames();

        loadUsers();

        //getGameOfUser();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void login(){
        userName = usernameInputText.getText().toString();
        password = passwordInputText.getText().toString();

        Log.i(TAG,"Click on Login Button");

        Call<Void> responseCall = api.login("Meritxell","holahola");
        //Call<Void> responseCall = api.login(userName,password);
        Log.i(TAG,"Get login for api");
        progressDialog.show();

        responseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    int codeResponse;
                    codeResponse = response.code();

                    if (codeResponse == 1) {
                        messageLogin = "User is banned";
                        loginSuccessful = false;
                        Toast toast = Toast.makeText(getApplicationContext(), "User is banned", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (codeResponse == 201) {
                        loginSuccessful = true;
                        messageLogin = "Successful";
                        Toast toast = Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (codeResponse == 3) {
                        isAdmin = true;
                        messageLogin = "User is admin";
                        loginSuccessful = true;
                        Toast toast = Toast.makeText(getApplicationContext(), "Successful, you're Admin", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        messageLogin = "Password incorrect";
                        loginSuccessful = false;
                        Toast toast = Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    progressDialog.hide();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG);
                    toast.show();
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),"Connection error!",Toast.LENGTH_LONG);
                toast.show();
                progressDialog.hide();
            }
        });


    }


    public void register(){
        userName = usernameInputText.getText().toString();
        password = passwordInputText.getText().toString();

        Log.i(TAG,"Click on Register Button");

        Call<Void> responseCall = api.register(userName,password);
        Log.i(TAG,"Get register for api");
        progressDialog.show();
        responseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int codeResponse;
                codeResponse = response.code();
                if (codeResponse == 1){
                    messageRegister = "Successful";
                    registerSuccessful = true;
                    Toast toast = Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG);
                    toast.show();
                } else if(codeResponse == 2){
                    messageRegister = "User already exists";
                    registerSuccessful = false;
                    Toast toast = Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_LONG);
                    toast.show();
                }else {
                    messageRegister = "Another error";
                    registerSuccessful = false;
                    Toast toast = Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG);
                    toast.show();
                }

                progressDialog.hide();

                if (registerSuccessful == true){
                    api.login(userName,password); //si nos registramos luego hacemos login
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_LONG);
                toast.show();

            }
        });
    }

    public void loadUsers(){
        Call<List<User>> userCall = api.loadUsers();

        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    List<User> userList = response.body();

                    for (int i = 0; i < userList.size(); i++){
                        Log.i(TAG, userList.get(i).getUserName());
                    }
                }
                else{
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
                }
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

    public void getListGames(){
        Call<List<Game>> gameCall = api.getListGames();

        gameCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (response.isSuccessful()){
                    List<Game> gameList = response.body();

                    for (int i = 0; i < gameList.size();i++){
                        Log.i(TAG,gameList.get(i).getNameGame());
                    }
                } else{
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
            public void onFailure(Call<List<Game>> call, Throwable t) {
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
