package s351927.oslomet.mappe2.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import s351927.oslomet.mappe2.Activtiy.PersonList;
import s351927.oslomet.mappe2.Activtiy.VisBestillingList;
import s351927.oslomet.mappe2.DB.DatabaseHandler;
import s351927.oslomet.mappe2.Modul.Bestilling;
import s351927.oslomet.mappe2.Modul.Kontakt;
import s351927.oslomet.mappe2.R;

public class NotifictionService extends Service {
    private static int MY_PERMISSIONS_REQUEST_SEND_SMS;
    private static int MY_PHONE_STATE_PERMISSION;
    private SharedPreferences sp;
    private DatabaseHandler db;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        db = new DatabaseHandler(getApplicationContext());
        boolean sjekkAkriverting = sp.getBoolean("AktivSms", false);
        String shareSMStid=sp.getString("sharetidSms","09:30");
        String shareNotitid=sp.getString("sharetidNoti","09:30");
        Log.d("shareTidNoti",shareNotitid+" "+shareSMStid);
        Calendar cal1= Calendar.getInstance();

        String currentime= new SimpleDateFormat("H:mm",Locale.getDefault()).format(new Date());
        Log.d("currentTime",currentime);
        String CurrrentDato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String shareSMSdato=sp.getString("sharedatoSms",CurrrentDato);
        Log.d("shareTidNoti",shareNotitid+" "+shareSMStid+shareSMSdato);
        List<Bestilling> bestillingList = db.HenteAlleBestilling();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, VisBestillingList.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        for (Bestilling bestilling : bestillingList) {
           // Toast.makeText( getApplicationContext(), CurrrentDato+" : " + bestilling.getDate()
                  //  , Toast.LENGTH_SHORT).show();
            if (bestilling.getDate().equals(CurrrentDato)) {
               // Toast.makeText( getApplicationContext(), "checkservice", Toast.LENGTH_SHORT).show();
                if(shareNotitid.equals(currentime)) {
                    Notification(pIntent, notificationManager);
                }
                Log.d("ShareSmsDato ,sjekkAktiv", shareSMSdato+sjekkAkriverting+shareSMStid );
                if (sjekkAkriverting && shareSMSdato.equals(CurrrentDato) && shareSMStid.equals(currentime) ) {
                    List<Kontakt> DeltakelserList = db.DeltakelserInIvitsjon(bestilling.getID());
                    if (DeltakelserList != null) {
                        for (Kontakt Deltaker : DeltakelserList) {
                            sendMelding(Deltaker.getNavn(),Deltaker.getTelefonnr());
                        }
                    }
                }
            }

        }
      //  db.close();
        return super.onStartCommand(intent, flags, startId);
    }

    private void Notification(PendingIntent pIntent, NotificationManager notificationManager) {
        SharedPreferences.Editor endreTid;
        endreTid = sp.edit();
        //nullstille Notitiden
        endreTid.putString("sharetidNoti", "23:59");
        endreTid.apply();
        String contentText = "Reminder! Det er en bestilling idag! ";
        Notification notification = new NotificationCompat.Builder(this,"channel_id")
                .setContentTitle("Resturant Bestilling")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.invitasjon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.invitasjon))
                .setContentIntent(pIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Objects.requireNonNull(notificationManager).notify(0, notification);
    }

    @SuppressLint("LongLogTag")
    private void sendMelding(String Navn, String telefonnr) {

        SharedPreferences.Editor endreTid;
        endreTid = sp.edit();
        //nullstille tiden
        endreTid.putString("sharetidSms", "23:59");
        endreTid.apply();




        String message = sp.getString("melding", "Reminder! Du har en Resturant invitasjon idag! .🍽");

        MY_PERMISSIONS_REQUEST_SEND_SMS =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission
                (this, Manifest.permission.READ_PHONE_STATE);
        if (MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED &&
                MY_PHONE_STATE_PERMISSION == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsMan = SmsManager.getDefault();
            smsMan.sendTextMessage(telefonnr,
                    null, message, null, null);
            Toast.makeText(this, "Melding ble sendt til: "+Navn, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Melding har ikke sendt", Toast.LENGTH_SHORT).show();
        }

    }
}
