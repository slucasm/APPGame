package dsa.eetac.upc.edu.appgame;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dsa.eetac.upc.edu.appgame.models.Game;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Game> dataset;
    private Context context;

    public Adapter(Context context){
        this.context = context;
        this.dataset = new ArrayList<>();
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

        public ViewHolder(View itemView){
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constrainLayout);
            TextView_name =  itemView.findViewById(R.id.textView_name);
            TextView_health = itemView.findViewById(R.id.textView_health);
            TextView_length = itemView.findViewById(R.id.textView_length);
        }
    }
}
