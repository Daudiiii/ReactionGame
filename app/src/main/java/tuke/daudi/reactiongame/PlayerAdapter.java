package tuke.daudi.reactiongame;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import tuke.daudi.reactiongame.RoomDB.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerHolder>{
    private List<Player> _data;
    private WeakReference<Context> _context;

    public PlayerAdapter(List<Player> data, WeakReference<Context> contextReference) {
        _context = contextReference;
        _data = data;
    }

    public void refreshData(List<Player> data){
        _data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(_context.get()).inflate(R.layout.player_item,viewGroup,false);
        return new PlayerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder playerHolder, int i) {

        playerHolder.nick.setText(_data.get(i).getNick());
        playerHolder.psc.setText(_data.get(i).getPsc());
        playerHolder.age.setText(_data.get(i).getAge());
        playerHolder.gender.setText(_data.get(i).getGender());
        playerHolder.glasses.setText(_data.get(i).getGlasses());
    }

    @Override
    public int getItemCount() {
        if(_data!=null)
            return _data.size();
        return 0;
    }
}
