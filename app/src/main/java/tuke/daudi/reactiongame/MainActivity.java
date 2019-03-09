package tuke.daudi.reactiongame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tuke.daudi.reactiongame.Location.AppLocationService;
import tuke.daudi.reactiongame.Location.LocationAddress;

public class MainActivity extends AppCompatActivity  implements ObviousSetup{
    private final int PERMISSION_LOCATION_ID = 1000;
    private ImageView start, stats, btnShowAddress;
    private Location location;
    private AppLocationService appLocationService;
    private BroadcastNetwork broadcastNetwork;
    private IntentFilter intentFilter;
    private DatabaseReference databaseReference0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadAll();
    }

    public void loadAll(){
        setViews();
        setDeclarations();
        setOnClickListeners();
        setupBroadcast();
    }

    public void setViews(){
        start = findViewById(R.id.ibtn_start);
        stats = findViewById(R.id.ibtn_highscores);
        btnShowAddress = findViewById(R.id.icon_gps_untracked);
    }

    public void setDeclarations(){
        appLocationService = new AppLocationService(this);
        location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        databaseReference0 = FirebaseDatabase.getInstance().getReference().child("players");
    }

    public void setOnClickListeners(){
        /* GPS Button*/

        /* Layout - hide soft input */
        findViewById(R.id.main_activity).setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;
        });
        /* Highscores Button*/
        stats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HighscoresActivity.class);
            startActivity(intent);
        });
        /* Start Button*/
        start.setOnClickListener(v -> {
            if (!((EditText) findViewById(R.id.et_nick)).getText().toString().isEmpty() && !((EditText) findViewById(R.id.et_age)).getText().toString().isEmpty() && !((EditText) findViewById(R.id.et_psc)).getText().toString().isEmpty()){
                String child = ((EditText) findViewById(R.id.et_nick)).getText().toString();
                DatabaseReference databaseReference = databaseReference0.child(child);
                databaseReference.child("nick").setValue(child);
                databaseReference.child("age").setValue(((EditText) findViewById(R.id.et_age)).getText().toString());
                databaseReference.child("psc").setValue(((EditText) findViewById(R.id.et_psc)).getText().toString());
                databaseReference.child("gender").setValue(((Spinner) findViewById(R.id.spinner_gender)).getSelectedItem().toString());
                databaseReference.child("glasses").setValue(((Spinner) findViewById(R.id.spinner_glasses)).getSelectedItem().toString());
                databaseReference.child("education").setValue(((Spinner) findViewById(R.id.spinner_edu)).getSelectedItem().toString());

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("child",child);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Vyplni vsetky polozky", Toast.LENGTH_SHORT).show();
            }

        });
        findViewById(R.id.logo_main).setOnClickListener(v -> fillInputs());
    }

    public void fillInputs(){
        ((EditText) findViewById(R.id.et_nick)).setText("Test");
        ((EditText) findViewById(R.id.et_age)).setText("54");
        ((EditText) findViewById(R.id.et_psc)).setText("076 62");
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            ((EditText)findViewById(R.id.et_psc)).setText(locationAddress);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_LOCATION_ID: {
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this,"Unable to get proper permission. You sure?",Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION_ID);
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastNetwork);
    }

    class BroadcastNetwork extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION_ID);
                return;
            }

            if(networkInfo != null && networkInfo.isConnected()){
                btnShowAddress.setImageResource(R.drawable.ic_gps_fixed);
                location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
                btnShowAddress.setOnClickListener(v -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LocationAddress.getAddressFromLocation(latitude, longitude,getApplicationContext(), new MainActivity.GeocoderHandler());
                    }

                });
            }else{
                btnShowAddress.setImageResource(R.drawable.ic_gps_off);
                btnShowAddress.setOnClickListener(v -> {
                    Toast.makeText(MainActivity.this, "Need Internet connection to fill PSC", Toast.LENGTH_LONG).show();
                });
                location = null;
            }
        }
    }

    public void setupBroadcast(){

        broadcastNetwork = new BroadcastNetwork();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(broadcastNetwork,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastNetwork);
        location = null;
        appLocationService = null;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

