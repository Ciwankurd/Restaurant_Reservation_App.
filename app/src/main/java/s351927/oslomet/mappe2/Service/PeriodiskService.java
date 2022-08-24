package s351927.oslomet.mappe2.Service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Objects;

public class PeriodiskService extends Service {
    private SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final int[] splittTidSMS = splitTid(sp.getString("sharetidSms", "15:05"));
        final int[] splittTidNoti = splitTid(sp.getString("sharetidNoti", "15:05"));

        java.util.Calendar calSMSTid = Calendar.getInstance();
        calSMSTid.set(Calendar.HOUR_OF_DAY, splittTidSMS[0]);
        calSMSTid.set(Calendar.MINUTE, splittTidSMS[1]);
        java.util.Calendar calNotiDato = Calendar.getInstance();
        calNotiDato.set(Calendar.HOUR_OF_DAY, splittTidNoti[0]);
        calNotiDato.set(Calendar.MINUTE, splittTidNoti[1]);

        Intent serviceIntent = new Intent(this, NotifictionService.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pIntent =
                PendingIntent.getService(this, 0, serviceIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(alarm).
                setExact(AlarmManager.RTC_WAKEUP, calSMSTid.getTimeInMillis(), pIntent);

        Intent serviceIntent1 = new Intent(this, NotifictionService.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pIntent1 =
                PendingIntent.getService(this, 1, serviceIntent1, 0);
        AlarmManager alarm1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(alarm1).
                setExact(AlarmManager.RTC_WAKEUP, calNotiDato.getTimeInMillis(), pIntent1);
        return super.onStartCommand(intent, flag, startId);
    }

    private int[] splitTid(String tid) {
        String[] splittTidListe = tid.split(":");
        return new int[]{Integer.parseInt(splittTidListe[0]), Integer.parseInt(splittTidListe[1])};
    }
}
