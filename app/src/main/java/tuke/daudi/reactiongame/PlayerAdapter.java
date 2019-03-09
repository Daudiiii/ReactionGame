package tuke.daudi.reactiongame;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>{
    private List<Player> _data;
//    private WeakReference<Context> _context;
    private DatabaseReference fireRef;
    public PlayerAdapter(List<Player> data) {
//        _context = contextReference;
        _data = data;
        fireRef = FirebaseDatabase.getInstance().getReference().child("players");
//        updatePlayers();
    }

    public void refreshData(List<Player> data){
        _data = data;
        notifyDataSetChanged();
    }



    private void updatePlayers(){
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Player player = dataSnapshot.getValue(Player.class);
                _data.add(player);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_item,viewGroup,false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder playerHolder, int i) {
        playerHolder.tvRank.setText(String.valueOf(i+1));
        playerHolder.tvNick.setText(_data.get(i).getNick());
        playerHolder.tvPsc.setText(_data.get(i).getPsc());
        playerHolder.tvScore.setText(String.valueOf(_data.get(i).getScore()));
    }

    @Override
    public int getItemCount() {
        if(_data!=null)
            return _data.size();
        return 0;
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder{

        public TextView tvRank, tvNick, tvPsc, tvScore;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.a2_tv_rank);
            tvNick = itemView.findViewById(R.id.a2_tv_nick);
            tvPsc = itemView.findViewById(R.id.a2_tv_psc);
            tvScore = itemView.findViewById(R.id.a2_tv_score);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }




}
