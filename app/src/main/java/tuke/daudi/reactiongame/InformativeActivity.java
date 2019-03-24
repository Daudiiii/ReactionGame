package tuke.daudi.reactiongame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class InformativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informative);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
          startActivity(new Intent(this,MainActivity.class));
        });
    }
}
