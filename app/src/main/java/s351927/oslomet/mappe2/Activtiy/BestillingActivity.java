package s351927.oslomet.mappe2.Activtiy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import s351927.oslomet.mappe2.DB.DatabaseHandler;
import s351927.oslomet.mappe2.Modul.Bestilling;
import s351927.oslomet.mappe2.Modul.Kontakt;
import s351927.oslomet.mappe2.Modul.Resturant;
import s351927.oslomet.mappe2.R;
import s351927.oslomet.mappe2.Service.NotifictionService;

public class BestillingActivity extends AppCompatActivity {

    List<Kontakt> kontakterDB;
    TextView velgPerson;
    private SharedPreferences sp;
    DatabaseHandler db;
    boolean[] selectbox;
    ArrayList<Long> PersonID = new ArrayList<>();
    ArrayList<Long> SelectedID = new ArrayList<>();
    String[] Personer;
    Calendar cal1;

    List<Resturant> resturanterDB;
    TextView velgResturant;
    ArrayList<Long> ResturantID = new ArrayList<>();
    Long Selected_ID_R;
    String[] Resturanter;

    TextView tvTid;
    TextView etDato;
    String date;
    String tid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bestilling);
        tvTid = findViewById(R.id.tv_select_time);
        etDato = findViewById(R.id.et_select_date);
        velgPerson = findViewById(R.id.Select_Personer);
        velgResturant = findViewById(R.id.Select_Resturant);
        cal1=Calendar.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(BestillingActivity.this);
        db = new DatabaseHandler(this);
        int k = 0;
        int m = 0;
        List<Resturant> resturantList = db.HenteAlleResturant();
        kontakterDB = db.HenteAlleKontakter();
        resturanterDB = db.HenteAlleResturant();
        Personer = new String[kontakterDB.size()];
        Resturanter = new String[resturanterDB.size()];
        for (Kontakt kontakt : kontakterDB) {

            Personer[k] = kontakt.getNavn();
            PersonID.add(kontakt.getID());
            k++;

        }
        for (Resturant resturant : resturanterDB) {
            Resturanter[m] = resturant.getNavn();
            ResturantID.add(resturant.getID());
            m++;
        }
        selectbox = new boolean[Personer.length];
//____________________****Personer Selection****________________________________________

        velgPerson.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BestillingActivity.this);

            builder.setTitle("Select Person");
            builder.setCancelable(false);
            if (kontakterDB.isEmpty()) {
                builder.setTitle("Det Må Registere noen Personer først!");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent d = new Intent(BestillingActivity.this, PersonActivity.class);
                    startActivity(d);
                });
                builder.setNegativeButton("Avbryte", (dialog, which) -> dialog.dismiss());

            }
            builder.setMultiChoiceItems(Personer, selectbox, (dialog, i, isChecked) -> {
                if (isChecked) {
                    SelectedID.add(PersonID.get(i));
                    Log.d("PersonID", PersonID.get(i) + "");
                    Collections.sort(SelectedID);

                } else {
                    SelectedID.remove(PersonID.get(i));
                }
                Log.d("PersonID", SelectedID + "");
            });
            builder.setPositiveButton("OK", (dialog, which) -> {

            });
            builder.setNegativeButton("Avbryte", (dialog, which) -> dialog.dismiss());
            builder.setNeutralButton("Nullstille", (dialog, which) -> {
                for (int i = 0; i < selectbox.length; i++) {
                    selectbox[i] = false;
                    SelectedID.clear();
                }
            });
            builder.show();
        });

//____________________****Resturant Selection****________________________________________


        velgResturant.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BestillingActivity.this);

            builder.setTitle("Select Resturant");
            builder.setCancelable(false);
            if (resturanterDB.isEmpty()) {
                builder.setTitle("Det Må Registere en Resturant først!");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    Intent d = new Intent(BestillingActivity.this, ResturantActivity.class);
                    startActivity(d);
                });
                builder.setNegativeButton("Avbryte", (dialog, which) -> dialog.dismiss());

            }


            final int[] defultpostion = {0};
            builder.setSingleChoiceItems(Resturanter, defultpostion[0], (dialog, which) -> {
                defultpostion[0] = which;
                Log.d("which_value", which + "");
                Selected_ID_R = ResturantID.get(which);
                Log.d("selectResturantID", Selected_ID_R + "");
                velgResturant.setText(Resturanter[which]);
            });


            builder.setPositiveButton("OK", (dialog, which) -> {

            });
            builder.setNegativeButton("Avbryte", (dialog, which) -> dialog.dismiss());

            builder.show();
        });
        // Velge Dato og Tiden
        Calendar calendar = Calendar.getInstance();
        final int Year = calendar.get(Calendar.YEAR);
        final int Month = calendar.get(Calendar.MONTH);
        final int Day = calendar.get(Calendar.DAY_OF_MONTH);
        final int Hour = calendar.get(Calendar.HOUR);
        final int Minute = calendar.get(Calendar.MINUTE);

        etDato.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BestillingActivity.this, (view, year, month, dayOfMonth) -> {
                        month = month + 1;
                        date =dayOfMonth + "/" + month + "/" + year;
                        etDato.setText(date);
                    },Year,Month,Day);

            datePickerDialog.show();

        });

        tvTid.setOnClickListener(v -> {
           TimePickerDialog timePickerDialog = new TimePickerDialog(BestillingActivity.this, (view, hourOfDay, minute) -> {
               tid = hourOfDay+":"+minute;
               tvTid.setText(tid);
           },Hour,Minute,true);
           timePickerDialog.show();

        });

    }
    // sende Bestiling til DB
    @SuppressLint("ResourceAsColor")
    public void sendBestilling(View view) {
        if(date!="" && tid!="" &&  !SelectedID.isEmpty() &&
                Selected_ID_R!=null) {
            Bestilling bestilling = new Bestilling(date, tid);
            db.RegisterBestilling(bestilling, Selected_ID_R, SelectedID);
            Toast toast = Toast.makeText(this, "VelLykket! Takk for Din Bestilling", Toast.LENGTH_SHORT);
            View backgeound = toast.getView();
            backgeound.setBackgroundColor(R.color.Khaki);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! Alle feltene må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @SuppressLint("ResourceAsColor")
    public void VisAlle(View view) {
        Intent d = new Intent(BestillingActivity.this, VisBestillingList.class);
        startActivity(d);
    }

    public void SendSMS(View view) {
        String currentime= (String) DateFormat.format("kk:mm", cal1.getTime());
        SharedPreferences.Editor endreTid;
        endreTid = sp.edit();
        //nullstille Notitiden
        endreTid.putString("sharetidSms",currentime);
        endreTid.apply();
        Intent intent = new Intent();
        intent.setAction("s351927.oslomet.mappe2.notificationbrodcastreceiver");
        sendBroadcast(intent);
    }
}
