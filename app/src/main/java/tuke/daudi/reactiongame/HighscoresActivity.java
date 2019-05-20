package tuke.daudi.reactiongame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HighscoresActivity extends AppCompatActivity implements ObviousSetup {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView home;
    private final List<Player> playerData = new ArrayList<>();
    private DatabaseReference firebaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_db_textviews);
        loadAll();
    }

    public void setOnClickListeners(){
        home.setOnClickListener(v -> {
            Intent intent = new Intent(HighscoresActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });

        findViewById(R.id.cl_highscores).setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        });
    }

    public void setViews(){
        recyclerView = findViewById(R.id.rv_data);
        home = findViewById(R.id.iv_filterHome);

    }

    @Override
    public void loadAll() {
        setViews();
        setOnClickListeners();
        setDeclarations();
    }

    @Override
    public void setDeclarations() {
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("players");
        layoutManager = new LinearLayoutManager(this);
        adapter = new PlayerAdapter(playerData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseReference.orderByChild("score").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null){
                    Player player = dataSnapshot.getValue(Player.class);
                    if (player.getScore() != null){
                        playerData.add(player);
                    }
                    adapter.notifyDataSetChanged();
                }
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
        });
    }
}


