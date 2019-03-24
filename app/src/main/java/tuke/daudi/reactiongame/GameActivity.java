package tuke.daudi.reactiongame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;

public class GameActivity extends AppCompatActivity implements ObviousSetup {
    private long timerForReaction, timeReacted;
    private Handler handler;
    private Button btn;
    private int maxWidth, maxHeight, reactionNumber;
    private ConstraintLayout layout;
    @SuppressLint("StaticFieldLeak")
    public static ImageView iv_react;
    public static Intent intentForRunnable;
    private List<Integer> listR;
    public List<Integer> listB;
    private ArrayList<GamePosition> positions;
    private DatabaseReference firebaseReference;
    private boolean two = false, three = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loadAll();
        Log.i("MyLog","Done");
    }

    @Override
    protected void onStart() {
        super.onStart();
        reactionNumber = 0;
    }

    public void loadAll() {
        setViews();
        setDeclarations();
        initializeDisplayResolution();
        setOnClickListeners();
        showFullScreen();
        createPositions();
    }

    public void setViews() {
        layout = findViewById(R.id.clayoutgame);
        layout.setBackgroundResource(R.color.black);
        btn = findViewById(R.id.test_btn);
        iv_react = findViewById(R.id.iv_react);
        iv_react.setVisibility(View.INVISIBLE);
    }

    public void setDeclarations() {
        listR = new ArrayList<>();
        listB = new ArrayList<>();
        handler = new Handler();
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("players").child(getIntent().getStringExtra("child")).child("reactions");
        reactionNumber = 0;
        intentForRunnable = getIntent();
    }


    public void setOnClickListeners() {
        btn.setOnClickListener(v -> {
            handler.postDelayed(gameRunnable, 3000);
            btn.setVisibility(GONE);
            timerForReaction = SystemClock.uptimeMillis();
            layout.setBackgroundResource(R.drawable.bg_one);
            findViewById(R.id.tv_activity_game_desc).setVisibility(GONE);
        });

        iv_react.setOnClickListener(v -> {
            if (iv_react.getVisibility() == View.VISIBLE) {
                iv_react.setVisibility(View.INVISIBLE);
                timeReacted = SystemClock.uptimeMillis() - timerForReaction;
                Log.i("MyLog", "Time Reacted : " + timeReacted);
                Log.i("MyLog", "Average score: " + calculateScore(listR));
                recordReaction(firebaseReference, iv_react.getX(), iv_react.getY(), timeReacted);
                reactionNumber++;
            } else {
                listB.add(2000);
            }

            swapBg();
        });

        layout.setOnClickListener(v -> iv_react.performClick());
    }

    public void swapBg(){
        if (positions.size() < 24 && positions.size() >= 12 && !two) {
            layout.setBackgroundResource(R.drawable.bg_two);
            two = true;
        }
        if(positions.size() < 12 && !three) {
            three = !three;
            layout.setBackgroundResource(R.drawable.bg_three);
        }
    }

    public void recordReaction(DatabaseReference FBPlayerReference, Float x, Float y, Long time) {
        listR.add(time.intValue());
        listB.add(time.intValue());
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("x").setValue(x);
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("y").setValue(y);
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("time").setValue(time);
    }

    public void recordReaction(DatabaseReference FBPlayerReference, Float x, Float y) {
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("x").setValue(x);
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("y").setValue(y);
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("time").setValue(-1);
    }

    private Long calculateScore(List<Integer> score) {
        return (long) score.stream().mapToInt(val -> val).average().orElse(0.0);
    }

    private void createPositions() {
        positions = new ArrayList<>();

        while(positions.size() < 36){
            int x = new Random().nextInt(maxWidth);
            int y = new Random().nextInt(maxHeight);
            x = (x >= (maxWidth - (maxWidth * 0.03))) ? (maxWidth - (int)(maxWidth * 0.03)) : x;
            y = (y >= (maxHeight - (maxHeight * 0.03))) ? (maxHeight - (int)(maxHeight *0.03)) : y;
            Log.i("MyLog","Max W H " + x + " " + y);
            positions.add(new GamePosition(x,y));
            Log.i("MyLog", "Size of positions list" + positions.size());
//            positions.add(new GamePosition(new Random().nextInt(maxWidth),new Random().nextInt(maxHeight)));
        }
    }

    public Runnable gameRunnable = new Runnable() {

        public void run() {

            if (!positions.isEmpty()) {
                int index = new Random().nextInt(positions.size());
                GamePosition pos = positions.get(index);
                positions.remove(index);
                swapBg();
                if (positions.size() != (35-listR.size())) {
                    recordReaction(firebaseReference, iv_react.getX(),iv_react.getY());
                    listR.add(calculateScore(listR).intValue());
                    reactionNumber++;
                }
                iv_react.setX(pos.getX());
                iv_react.setY(pos.getY());
                iv_react.setVisibility(View.VISIBLE);
                timerForReaction = SystemClock.uptimeMillis();
                int delay = (int) (new Random().nextFloat() + 1) * 1500;
                handler.postDelayed(this, delay);
            } else {
                handler.removeCallbacks(this);
                Intent i = new Intent(GameActivity.this, EndGameActivity.class);
                String nick = intentForRunnable.getStringExtra("child");
                FirebaseDatabase.getInstance().getReference().child("players")
                        .child(nick).child("score").setValue(calculateScore(listB));
                i.putExtra("score", String.valueOf(calculateScore(listB)));
                i.putExtra("nick", nick);
                startActivity(i);
                GameActivity.this.finish();
            }
        }

    };

    public void showFullScreen() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public void initializeDisplayResolution() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        maxWidth = size.x - 40;
        maxHeight = size.y - 60;
    }
}