package s351927.oslomet.mappe2.Activtiy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import s351927.oslomet.mappe2.R;
import s351927.oslomet.mappe2.Service.NotifictionService;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //hente Btn
        Button resturant=findViewById(R.id.Registere_Restursnt);
        Button person=findViewById(R.id.Registere_person);
        Button bestilling=findViewById(R.id.Bestilling);
        Button innstilling=findViewById(R.id.Innstilling);

        resturant.setOnClickListener(view ->{
            Intent d = new Intent(MainActivity.this,ResturantActivity.class);
            startActivity(d);
        });
        person.setOnClickListener(view ->{
            Intent d = new Intent(MainActivity.this,PersonActivity.class);
            startActivity(d);
        });
        bestilling.setOnClickListener(view ->{
            Intent d = new Intent(MainActivity.this,BestillingActivity.class);
            startActivity(d);
        });
        innstilling.setOnClickListener(view ->{
           Intent d = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(d);
            /*Intent k =new Intent(MainActivity.this, NotifictionService.class);
            startService(k);*/
        });
       // buildNotificationService();
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("vil du ha slutte appen?")
                .setCancelable(false)
                .setPositiveButton("ja", (dialog, id) -> MainActivity.super.onBackPressed())
                .setNegativeButton("Nei", null)
                .show();
    }
/*
    private void buildNotificationService() {
        Intent intent = new Intent();
        intent.setAction("s351927.oslomet.mappe2.notificationbrodcastreceiver");
        sendBroadcast(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
        buildNotificationService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        buildNotificationService();
    }

 */
}