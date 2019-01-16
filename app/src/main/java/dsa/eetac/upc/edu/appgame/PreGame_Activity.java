package dsa.eetac.upc.edu.appgame;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreGame_Activity extends AppCompatActivity {

    Button newGame;
    Button loadGame;
    Button logOut;

    private API api;


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


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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




}
