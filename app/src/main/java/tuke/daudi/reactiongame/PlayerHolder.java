package tuke.daudi.reactiongame;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PlayerHolder extends RecyclerView.ViewHolder {
    public TextView nick,gender,age,psc,glasses;

    public PlayerHolder(@NonNull View itemView) {
        super(itemView);
        nick = itemView.findViewById(R.id.a2_tv_nick);
        gender = itemView.findViewById(R.id.a2_tv_gender);
        age = itemView.findViewById(R.id.a2_tv_age);
        psc = itemView.findViewById(R.id.a2_tv_psc);
        glasses = itemView.findViewById(R.id.a2_tv_glasses);
    }
}
