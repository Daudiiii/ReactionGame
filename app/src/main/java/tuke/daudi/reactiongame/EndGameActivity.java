package tuke.daudi.reactiongame;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import static tuke.daudi.reactiongame.App.CHANNEL_1_ID;

public class EndGameActivity extends AppCompatActivity implements ObviousSetup{
    private TextView tv_score;
    private Button scores, home;
    private NotificationManagerCompat notificationManager;
    private String nick, score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Intent i = getIntent();
        loadAll();
        nick = i.getStringExtra("nick");
        score = i.getStringExtra("score");
        Log.i("MyLog",score + "");
        tv_score.setText(score);
        sendOnChannel1();
    }

    @Override
    public void loadAll() {
        setViews();
        setDeclarations();
        setOnClickListeners();
    }

    public void setViews(){
        tv_score = findViewById(R.id.tv_endgame_result);
        scores = findViewById(R.id.btn_endgame_highscores);
        home = findViewById(R.id.btn_endgame_home);

    }

    public void setOnClickListeners(){
        scores.setOnClickListener(v -> {
            Intent intent = new Intent(EndGameActivity.this, HighscoresActivity.class);
            finish();
            startActivity(intent);
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });

    }

    @Override
    public void setDeclarations() {
        notificationManager = NotificationManagerCompat.from(this);
    }

    public void sendOnChannel1(){
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0 , intent, 0);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_app)
                .setContentTitle("Posledná hra")
                .setContentText("Hráč " + nick + " dosiahol score: " + score + "\n Chceš ho prekonať ?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .addAction(R.mipmap.ic_launcher, "Prekonať",contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }
}
