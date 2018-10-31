package tuke.daudi.reactiongame;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveToCsv();
//                Toast.makeText(getApplicationContext(),"pes", Toast.LENGTH_LONG).show();
//                txt.setText(list.get(list.size()-1).getNick());
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
//        List<Player> output = null;
//        try {
//            output = dbGetData.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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

//    public void saveToCsv(){
//        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//        String fileName = "data.csv";
//        String filePath = baseDir + File.separator + fileName;
//
//        File f = new File(filePath);
//        CSVWriter writer = null;
//        FileWriter mFileWriter = null;
//        // File exist
//        if(f.exists() && !f.isDirectory()){
//            try {
//                mFileWriter = new FileWriter(filePath , true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            writer = new CSVWriter(mFileWriter);
//        }
//        else {
//            try {
//                writer = new CSVWriter(new FileWriter(filePath));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        String[] data = {"Ship Name","Scientist Name", "..."};
//
//        writer.writeNext(data);
//
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
