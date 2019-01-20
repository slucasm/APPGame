package dsa.eetac.upc.edu.appgame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;
import dsa.eetac.upc.edu.appgame.models.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Game> dataset;
    private Context context;

    private String userName;

    private API api;

    ProgressDialog progressDialog;

    public Adapter(Context context,String userName){
        this.context = context;
        this.dataset = new ArrayList<>();
        api = API.createAPI();
        this.userName = userName;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( Adapter.ViewHolder holder, int position) {
        Game game = dataset.get(position);
        holder.TextView_name.setText(game.getNameGame().toString());
        holder.TextView_health.setText(Integer.toString(game.getHealthPoints()));
        holder.TextView_length.setText(Integer.toString(game.getGameLength()));

        int isCompleted = game.getIsCompleted();

        if (isCompleted == 1){
            holder.checkBox.setChecked(true);
        } else if(isCompleted == 0){
            holder.checkBox.setChecked(false);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Loading...");
                progressDialog.setMessage("Waiting for the server");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                TextView textView = v.findViewById(R.id.textView_name);
                //Toast toast = Toast.makeText(context,textView.getText().toString(),Toast.LENGTH_LONG);
                //toast.show();
                String partida = textView.getText().toString();
                putUserAndGame(userName,partida);
                Intent intent = new Intent(context,GameActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addElements(List<Game> listGames){
        dataset.addAll(listGames);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout constraintLayout;

        private TextView TextView_name;
        private TextView TextView_health;
        private TextView TextView_length;
        private CheckBox checkBox;

        public ViewHolder(View itemView){
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constrainLayout);
            TextView_name =  itemView.findViewById(R.id.textView_name);
            TextView_health = itemView.findViewById(R.id.textView_health);
            TextView_length = itemView.findViewById(R.id.textView_length);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    public void putUserAndGame(String userName,String gameName){
        Call<Respuesta> respuestaCall = api.putUserAndGame(userName, gameName);
        respuestaCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = response.body();
                progressDialog.hide();
                int code = respuesta.getCode();
                if (code == 1){
                    Toast toast = Toast.makeText(context,respuesta.getMessage(),Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }
}
