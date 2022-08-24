package s351927.oslomet.mappe2.Service;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class NotificationBrodcastReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "I BroadcastReceiver", Toast.LENGTH_SHORT).show();
        //Vil sende det videre til å velge tidspunkt til å sende sms
        Intent intent1 = new Intent(context, PeriodiskService.class);
       context.startService(intent1);
    }
}
