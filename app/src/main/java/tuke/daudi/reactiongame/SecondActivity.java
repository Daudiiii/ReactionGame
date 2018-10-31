package tuke.daudi.reactiongame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tuke.daudi.reactiongame.RoomDB.Player;

public class SecondActivity extends AppCompatActivity {
    private DbGetData data;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<PlayerHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_db_textviews);
//        TextView txt = findViewById(R.id.tv_output_db);
        data = new DbGetData(new WeakReference<Context>(getApplicationContext()));
        recyclerView = findViewById(R.id.rv_movie_view);
        layoutManager = new LinearLayoutManager(this);
        List<Player> playerData = null;
        try {
            playerData = data.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new PlayerAdapter(playerData,new WeakReference<Context>(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        List<Player> data = null;
//        try {
//            data = data.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        String out = data.get(data.size()-1).getAge() + "\n"
//                + data.get(data.size()-1).getNick() + "\n"
//                + data.get(data.size()-1).getGender() + "\n"
//                + data.get(data.size()-1).getGlasses() + "\n"
//                + data.get(data.size()-1).getPsc() + "\n"
//                + data.get(data.size()-1).getId();
//        txt.setText(out);
    }
}


