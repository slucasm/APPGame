package dsa.eetac.upc.edu.appgame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadGamesActivity extends AppCompatActivity {

    private Adapter recycler;
    private RecyclerView recyclerView;

    private API api;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_games);

        toolbar = findViewById(R.id.toolbar);

        api = API.createAPI();

        String userName = getIntent().getExtras().getString("userName");
        String password = getIntent().getExtras().getString("password");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Adapter(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadGamesActivity.this, PreGame_Activity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
                finish();
            }
        });

        getListGamesOfUser(userName);
    }


    List<Game> gameList;

    public void getListGamesOfUser(String userName){
        progressDialog.show();
        Call<List<Game>> gameCall = api.getListGamesOfUser(userName);

        gameCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                //if (response.isSuccessful()){
                    gameList = response.body();
                    progressDialog.hide();
                    if(gameList.size() > 0){
                        recycler.addElements(gameList);
                    }
                    else{
                        Toast toast =  Toast.makeText(getApplicationContext(),"There aren't games",Toast.LENGTH_LONG);
                        toast.show();
                    }
                //}
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                    progressDialog.hide();
            }
        });
    }
}
