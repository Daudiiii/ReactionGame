package tuke.daudi.reactiongame;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PlayerHolder extends RecyclerView.ViewHolder {
    public TextView rank,nick,psc,score;

    public PlayerHolder(@NonNull View itemView) {
        super(itemView);
        rank = itemView.findViewById(R.id.a2_tv_rank);
        nick = itemView.findViewById(R.id.a2_tv_nick);
        psc = itemView.findViewById(R.id.a2_tv_psc);
        score = itemView.findViewById(R.id.a2_tv_score);
    }
}
