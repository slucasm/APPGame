package dsa.eetac.upc.edu.appgame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import dsa.eetac.upc.edu.appgame.models.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreGame_Activity extends AppCompatActivity {

    Button newGame;
    Button loadGame;
    Button logOut;

    private API api;

    EditText input;
    Button createGame;

    String gameName;

    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);

        newGame = findViewById(R.id.button_newgame);
        loadGame = findViewById(R.id.button_loadgame);
        logOut = findViewById(R.id.button_logout);

        api = API.createAPI();

        String userName = getIntent().getExtras().getString("userName");
        String password = getIntent().getExtras().getString("password");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreGame_Activity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCreateGame(userName);


            }
        });

        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreGame_Activity.this, LoadGamesActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("password",password);
                startActivity(intent);
                finish();
            }
        });
    }


    public void alertDialogCreateGame(String userName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Write game name");
        builder.setMessage("Pleas fill to create new game");

        input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameName = input.getText().toString();
                newGame(userName,gameName);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void newGame(String userName,String gameName){
        progressDialog.show();
        Call<Respuesta> respuestaCall = api.newGame(userName, gameName);

        respuestaCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                progressDialog.hide();
                int code = respuesta.getCode();
                String message = respuesta.getMessage();
                if (code == 1){
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                } else if(code == 3){
                    Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                progressDialog.hide();
                Toast toast = Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
