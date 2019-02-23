package tuke.daudi.reactiongame;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import tuke.daudi.reactiongame.RoomDB.Player;

import static tuke.daudi.reactiongame.App.CHANNEL_1_ID;
import static tuke.daudi.reactiongame.App.CHANNEL_2_ID;

public class EndGameActivity extends AppCompatActivity implements ObviousSetup{
    private TextView tv_score;
    private Button scores, home;
    private final int PERMISSION_ID_ACCESS_STORAGE = 1000;
    private List<String> list;
    private NotificationManagerCompat notificationManager;
    private String nick, score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        if(ActivityCompat.checkSelfPermission(EndGameActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EndGameActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_ID_ACCESS_STORAGE);
        }
        loadAll();
        addToDatabase();



    }

    @Override
    public void loadAll() {
        setViews();
        setDeclarations();
        setOnClickListeners();
    }

    public void addToDatabase(){
        DbGetData dbGetData = new DbGetData(new WeakReference<>(getApplicationContext()));
        Intent i = getIntent();
        list = i.getStringArrayListExtra("userArr");
//        Double score = Objects.requireNonNull(i.getExtras()).getDouble("score");
        //nick, age, psc, gender, glasses, score
        final Player player = new Player(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4),list.get(5));
        dbGetData.execute(player);
        tv_score.setText(list.get(5));
        nick = list.get(0);
        score = list.get(5);
        sendOnChannel1();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_ID_ACCESS_STORAGE:{
                for(int i = 0;i < permissions.length;i++){
                    if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"Writing to file ...",Toast.LENGTH_LONG).show();
                        }
                        else{
                            //In real cases, this string should not be hardcoded and would be places inside the values/strings.xml file
                            Toast.makeText(this,"Unable to get permissions, have to quit.",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
        }
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
//
//    public void sendOnChannel2(){
//        Notification notification = new NotificationCompat.Builder(this,CHANNEL_2_ID)
//                .setSmallIcon(R.drawable.ic_notification_app)
//                .setContentTitle("Posledná hra")
//                .setContentText("Hráč " + nick + " dosiahol score: " + score + "\n Chceš ho prekonať ?")
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .build();
//        notificationManager.notify(2, notification);
//    }


}
