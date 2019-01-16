package dsa.eetac.upc.edu.appgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadGamesActivity extends AppCompatActivity {

    private Adapter recycler;
    private RecyclerView recyclerView;

    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_games);

        api = API.createAPI();

        String userName = getIntent().getExtras().getString("userName");
        String password = getIntent().getExtras().getString("password");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Adapter(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getListGamesOfUser(userName);
    }


    List<Game> gameList;

    public void getListGamesOfUser(String userName){
        Call<List<Game>> gameCall = api.getListGamesOfUser(userName);
        gameCall.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                //if (response.isSuccessful()){
                    gameList = response.body();

                    if(gameList.size() > 0){
                        recycler.addElements(gameList);
                    }
                //}
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {

            }
        });
    }
}
