package tuke.daudi.reactiongame;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tuke.daudi.reactiongame.RoomDB.DBTools;
import tuke.daudi.reactiongame.RoomDB.Player;

public class MainActivity extends AppCompatActivity {
    private DbGetData dbGetData;
    private List<Player> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton start = findViewById(R.id.ibtn_start);
        ImageButton stats = findViewById(R.id.ibtn_highscores);
        final TextView txt = findViewById(R.id.textView);
        dbGetData = new DbGetData(new WeakReference<Context>(getApplicationContext()));

        findViewById(R.id.main_activity).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psc = ((EditText)findViewById(R.id.et_psc)).getText().toString();
                String nick = ((EditText)findViewById(R.id.et_nick)).getText().toString();
                String age = ((Spinner)findViewById(R.id.spinner_age)).getSelectedItem().toString();
                String gender = ((Spinner)findViewById(R.id.spinner_gender)).getSelectedItem().toString();
                String glasses = ((Spinner)findViewById(R.id.spinner_glasses)).getSelectedItem().toString();
                final Player player = new Player(psc,nick,age,gender,glasses);
                dbGetData.execute(player);

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }

}
