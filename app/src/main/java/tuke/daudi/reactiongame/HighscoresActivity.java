package tuke.daudi.reactiongame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tuke.daudi.reactiongame.RoomDB.Player;

public class HighscoresActivity extends AppCompatActivity {
    private DbGetData data;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<PlayerHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText nick, psc;
    private Button search;
    private ImageView home;
    private List<Player> playerData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_db_textviews);
        data = new DbGetData(new WeakReference<>(getApplicationContext()));
        layoutSetup();
        loadOnClickListeners();
        layoutManager = new LinearLayoutManager(this);
        playerData = null;
        try {
            playerData = data.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new PlayerAdapter(playerData,new WeakReference<>(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void loadOnClickListeners(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager = new LinearLayoutManager(HighscoresActivity.this);
                data = new DbGetData(new WeakReference<>(HighscoresActivity.this.getApplicationContext()));
                data.setParams(nick.getText().toString(),psc.getText().toString());
                try {
                    playerData = data.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter = new PlayerAdapter(playerData, new WeakReference<>(HighscoresActivity.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighscoresActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        findViewById(R.id.cl_highscores).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    public void layoutSetup(){
        recyclerView = findViewById(R.id.rv_data);
        nick = findViewById(R.id.et_filterNick);
        psc = findViewById(R.id.et_filterPsc);
        search = findViewById(R.id.btn_search);
        home = findViewById(R.id.iv_filterHome);
        nick.setText("");
        psc.setText("");
    }


}


