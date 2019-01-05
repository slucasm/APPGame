package dsa.eetac.upc.edu.appgame;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = usernameInputText.getText().toString();
                password = passwordInputText.getText().toString();

                Call<Response> responseCall = api.login(userName,password);
                progressDialog.show();
                responseCall.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, Response<Response> response) {
                        int codeResponse;
                        codeResponse = response.code();

                        if (codeResponse == 1) {
                            messageLogin = "User is banned";
                            loginSuccessful = false;
                            Toast toast = Toast.makeText(getApplicationContext(),"User is banned",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else if (codeResponse == 2) {
                            loginSuccessful = true;
                            messageLogin = "Successful";
                            Toast toast = Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else if (codeResponse == 3) {
                            isAdmin = true;
                            messageLogin = "User is admin";
                            loginSuccessful = true;
                            Toast toast = Toast.makeText(getApplicationContext(),"Successful, you're Admin",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else{
                            messageLogin = "Password incorrect";
                            loginSuccessful = false;
                            Toast toast = Toast.makeText(getApplicationContext(),"Password incorrect",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        progressDialog.hide();
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Connection error!",Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = usernameInputText.getText().toString();
                password = passwordInputText.getText().toString();

                Call<Response> responseCall = api.register(userName,password);
                progressDialog.show();
                responseCall.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, Response<Response> response) {
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
                        }else if (codeResponse == 3){
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
                    public void onFailure(Call<Response> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Connection error",Toast.LENGTH_LONG);
                        toast.show();

                    }
                });
            }
        });
    }


}
