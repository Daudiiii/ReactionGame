package tuke.daudi.reactiongame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;

public class GameActivity extends AppCompatActivity implements ObviousSetup{
    private long timerForReaction, timeReacted;
    private Handler handler;
    private Button btn;
    private int maxWidth, maxHeight, reactionNumber;
    private ConstraintLayout layout;
    public static ImageView iv_react;
    public static Intent intentForRunnable;
    private final String csvFile = "data.csv";
    private List<Integer> listR;
    private ArrayList<String> s;
    private ArrayList<GamePosition> positions;
    private DatabaseReference firebaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loadAll();
    }



    public void loadAll(){
        setViews();
        setDeclarations();
        initializeDisplayResolution();
        setOnClickListeners();
        showFullScreen();
        createPositions();
    }

    public void setViews(){
        layout = findViewById(R.id.clayoutgame);
        layout.setBackgroundResource(R.color.black);
        btn = findViewById(R.id.test_btn);
        iv_react = findViewById(R.id.iv_react);
        iv_react.setVisibility(View.INVISIBLE);
    }

    public void setDeclarations(){
        listR = new ArrayList<>();
        handler = new Handler();
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("players").child(getIntent().getStringExtra("child")).child("reactions");
        reactionNumber = 0;
        intentForRunnable = getIntent();
    }



    public void setOnClickListeners(){
        btn.setOnClickListener(v -> {
            handler.postDelayed(gameRunnable,3000);
            btn.setVisibility(GONE);
            timerForReaction = SystemClock.uptimeMillis();
            layout.setBackgroundResource(R.drawable.background_game);
            findViewById(R.id.tv_activity_game_desc).setVisibility(GONE);
        });

        iv_react.setOnClickListener(v -> {
            if(iv_react.getVisibility() == View.VISIBLE){
                iv_react.setVisibility(View.INVISIBLE);
                timeReacted = SystemClock.uptimeMillis() - timerForReaction;
                Log.i("MyLog","Time Reacted : " + timeReacted);
                Log.i("MyLog","Average score: " + calculateScore(listR));
                recordReaction(firebaseReference,iv_react.getX(), iv_react.getY(), timeReacted);
            }else{
                recordReaction(firebaseReference,iv_react.getX(), iv_react.getY(), 2000L);
            }
            reactionNumber++;
        });

        layout.setOnClickListener(v -> iv_react.performClick());
    }

    private void recordReaction(DatabaseReference FBPlayerReference, Float x, Float y, Long time){
        listR.add(time.intValue());
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("x").setValue(x);
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("y").setValue(y);
        FBPlayerReference.child(String.valueOf(reactionNumber)).child("time").setValue(time);
    }

    private Long calculateScore(List<Integer> score){
        return (long)score.stream().mapToInt(val -> val).average().orElse(0.0);
    }

    private void createPositions(){
        positions = new ArrayList<>();

        for(int y = 5; y <= 95; y+=30){
            for(int x = 5; x <= 95; x+=30){
                positions.add(new GamePosition((maxWidth/100)*x, (maxHeight/100)*y));

            }
            positions.add(new GamePosition(
                    new Random().nextInt(maxWidth) >= maxWidth - iv_react.getWidth()  ? maxWidth - iv_react.getWidth() : new Random().nextInt(maxWidth),
                    new Random().nextInt(maxHeight) >= maxHeight - iv_react.getHeight() ? maxHeight - iv_react.getHeight() : new Random().nextInt(maxHeight)
            ));
        }
        Log.i("MyLog","Size of positions list" + positions.size());
        positions = new ArrayList<>();
        positions.add(new GamePosition(45,65));
        positions.add(new GamePosition(453,653));
        positions.add(new GamePosition(245,265));
    }

    public Runnable gameRunnable = new Runnable() {

        public void run() {

            if(!positions.isEmpty()){
                int index = new Random().nextInt(positions.size());
                GamePosition pos = positions.get(index);
                positions.remove(index);
                iv_react.setX(pos.getX()); iv_react.setY(pos.getY()); iv_react.setVisibility(View.VISIBLE);
                timerForReaction = SystemClock.uptimeMillis();
                int delay = (int)(new Random().nextFloat()+1)*1500;
                handler.postDelayed(this,  delay);
//                if (timerForReaction == delay){
//                    Log.i("MyLog","It works maybe /autoreaction/");
//                    iv_react.performClick();
//                    handler.postDelayed(this,  (int)(new Random().nextFloat()+1)*1500);
//                }
            }else{
                handler.removeCallbacks(this);
                Intent i = new Intent(GameActivity.this,EndGameActivity.class);
                String nick = intentForRunnable.getStringExtra("child");
                FirebaseDatabase.getInstance().getReference().child("players")
                        .child(nick).child("score").setValue(calculateScore(listR));
                i.putExtra("score",String.valueOf(calculateScore(listR)));
                i.putExtra("nick",nick);

//                ArrayList<String> s = intent.getStringArrayListExtra("userArr");
//                s.add(String.valueOf(calculateScore(listR)));
//                i.putStringArrayListExtra("userArr",intent.getStringArrayListExtra("userArr"));
                startActivity(i);
                GameActivity.this.finish();
            }
        }

    };


    public void showFullScreen() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public void hideNavBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void changePosition(View view){

        int rx = new Random().nextInt(maxWidth);
        int ry = new Random().nextInt(maxHeight);

        view.setX(rx < 0 || rx >= maxWidth - view.getWidth()  ? maxWidth - view.getWidth() : rx);
        view.setY(ry < 0 || ry >= maxHeight - view.getHeight() ? maxHeight - view.getHeight() : ry);



        Log.i("MyLog","X SIZE : " + iv_react.getX());
        Log.i("MyLog","Y SIZE : " + iv_react.getY());
        iv_react.setVisibility(View.VISIBLE);
    }

    public void initializeDisplayResolution(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        maxWidth = size.x;
        maxHeight = size.y;
    }


    public void write(String nick, String age, String psc, String gender,
                      String glass, float x, float y, long time)
    {
        try
        {
            File f = new File(Environment.getExternalStorageDirectory().toString() + "/csv");
            if (!f.exists()){
                f.mkdirs();
            }
            File file = new File(f, csvFile);
            FileWriter writer = new FileWriter(file, true);
            if(!file.exists()){
                writer.append("nick, age, psc, gender, glass, x, y, time \n");
            }
            writer.append(nick + ", " + age + ", " + psc + ", " + gender + ", " + glass
                    + " , " + x + ", " + y+ ", " +time + "\n");
            Log.i("MyLog",nick + ", " + age + ", " + psc + ", " + gender + ", " + glass
                    + " , " + x + ", " + y+ ", " +time + " was added to csv");
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
/* Old game runnable

            timerForGame = SystemClock.uptimeMillis() - startTimer;
            sec = ((int) (timerForGame / 1000))%60;
            Log.i("MyLog","Seconds: "+ sec);
            if(sec > 2 && sec < 9){
                changePosition(iv_react);
                timerForReaction = SystemClock.uptimeMillis();
            }
            if (sec == 9){
                layout.setBackgroundColor(Color.parseColor("#000000"));
                tv_endGame.setVisibility(View.VISIBLE);
                tv_endgame_score.setText("Score: " + calculateScore(listR));
                tv_endgame_score.setVisibility(View.VISIBLE);
            }
            if(sec > 9){
                handler.removeCallbacks(this);
                Intent i = new Intent(GameActivity.this,EndGameActivity.class);
                Intent intent = getIntent();
                ArrayList<String> s = intent.getStringArrayListExtra("userArr");
                s.add(String.valueOf(calculateScore(listR)));
                i.putStringArrayListExtra("userArr",intent.getStringArrayListExtra("userArr"));
                startActivity(i);
                GameActivity.this.finish();

            }else{
                handler.postDelayed(this,  (int)(new Random().nextFloat()+1)*1500);
            }*/