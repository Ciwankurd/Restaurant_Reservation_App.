package s351927.oslomet.mappe2.Fragmenter;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Calendar;
import java.util.Objects;

import s351927.oslomet.mappe2.Activtiy.BestillingActivity;
import s351927.oslomet.mappe2.R;

public class SettingsPerferences extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    boolean aktivert;
    private Calendar calendar;
    private String sharetidSMS;
    private String sharedatoSMS;
    private String sharetidNoti;
    private String sharemelding;
    EditTextPreference melding;
    EditTextPreference SMStid;
    EditTextPreference SMSdato;
    EditTextPreference Notitid;
    SwitchPreferenceCompat aktivertsjekk;
    private SharedPreferences sharep;
    private boolean tilat;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        aktivertsjekk = findPreference("aktiv");
        SMSdato= findPreference("Sms_date");
        SMStid = findPreference("Sms_time");
        Notitid = findPreference("Noti_time");
        melding = findPreference("Egen_melding");
        sharemelding = melding.getText().toString();
        Log.d("egenMelding",sharemelding);
        sharep = PreferenceManager.getDefaultSharedPreferences(getContext());
        calendar = Calendar.getInstance();
        final int Year = calendar.get(Calendar.YEAR);
        final int Month = calendar.get(Calendar.MONTH);
        final int Day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        final int minut = calendar.get(java.util.Calendar.MINUTE);
        //lagre Tid
        // tid.setDefaultValue(hour+":"+minut);
        SMStid.setOnPreferenceClickListener(preference -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(), (view, hour1, min) -> {
                        String tiden = hour1 + ":" + min;
                        SMStid.setSummary(tiden);
                        SMStid.setText(tiden);
                        sharetidSMS = hour1 + ":" + min;
                        sharep.edit().putString("sharetidSms", sharetidSMS).apply();
                        buildNotificationService();

                    }, hour, minut, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
            return true;
        });
        Notitid.setOnPreferenceClickListener(preference -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(), (view, hour12, min) -> {
                        String tiden = hour12 + ":" + min;
                        Notitid.setSummary(tiden);
                        Notitid.setText(tiden);
                        sharetidNoti = hour12 + ":" + min;
                        sharep.edit().putString("sharetidNoti", sharetidNoti).apply();
                        buildNotificationService();
                    }, hour, minut, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();

            return true;
        });
        SMSdato.setOnPreferenceClickListener(preference -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), (view, year, month, dayOfMonth) -> {
                month = month + 1;
                sharedatoSMS =dayOfMonth + "/" + month + "/" + year;
                SMSdato.setText(sharedatoSMS);
                SMSdato.setSummary(sharedatoSMS);
                sharep.edit().putString("sharedatoSms",sharedatoSMS).apply();
                buildNotificationService();
            },Year,Month,Day);

            datePickerDialog.show();
            return true;
        });
        // lagre Melding
        melding.setOnPreferenceClickListener(preference -> {
            sharep.edit().putString("melding",sharemelding).apply();
            return false;
        });

        aktivertsjekk.setOnPreferenceChangeListener((preference, newValue) -> {

                boolean sjekkAVPÅSms = aktivertsjekk.isEnabled();
                if (sjekkAVPÅSms) {
                    if(!tilat) {
                        Tillatelse();
                        tilat=true;
                        sharep.edit().putBoolean("tilat",tilat);
                    }
                    if(aktivert) {
                        sharep.edit().putBoolean("AktivSms", aktivert).apply();
                    }
                } else {
                    sharep.edit().putBoolean("AktivSms", false).apply();
                }

            return true;
        });


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       if (key.equals("aktiv")) {
            boolean sjekkAVPÅSms = sharedPreferences.getBoolean("AktivSms", false);
            if (sjekkAVPÅSms) {
                if(aktivert) {
                    sharep.edit().putBoolean("AktivSms", aktivert).apply();

                }
                else {
                    sharep.edit().putBoolean("AktivSms", false).apply();
                }
            } else {
                sharep.edit().putBoolean("AktivSms", false).apply();
            }
        }


    }



    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void Tillatelse() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Vi trenger din tillatelse")
                .setMessage("Vil du gi tillatelse til denen appen til å sende sms?")
                .setPositiveButton("Ja", (dialog, which) -> {
                    ActivityCompat.requestPermissions(SettingsPerferences.this.getActivity(),
                            new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 1);
                    if(aktivertsjekk.isEnabled()) {
                        aktivert = true;
                    }
                })

                .setNegativeButton("Nei", (dialog, which) -> {
                    ActivityCompat.requestPermissions(SettingsPerferences.this.getActivity(),
                            new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 0);
                    aktivert = false;

                    dialog.dismiss();

                })
                .create()
                .show();
    }

    private void buildNotificationService() {
        Intent intent = new Intent();
        intent.setAction("s351927.oslomet.mappe2.notificationbrodcastreceiver");
        requireActivity().sendBroadcast(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
        aktivert = aktivertsjekk.isEnabled();
        sharep.edit().putString("melding", sharemelding).apply();
        sharep.edit().putBoolean("AktivSms", aktivert).apply();
        sharep.edit().putString("sharetidNoti", sharetidNoti).apply();
        sharep.edit().putString("sharetidSms", sharetidSMS).apply();
        sharep.edit().putString("sharedatoSms", sharedatoSMS).apply();
        if(aktivert) {
            buildNotificationService();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        aktivert = aktivertsjekk.isEnabled();
        sharep.edit().putString("melding", sharemelding).apply();
        sharep.edit().putBoolean("AktivSms", aktivert).apply();
        sharep.edit().putString("sharetidNoti", sharetidNoti).apply();
        sharep.edit().putString("sharetidSms", sharetidSMS).apply();
        sharep.edit().putString("sharedatoSms", sharedatoSMS).apply();
        if(aktivert) {
            buildNotificationService();
        }
    }


}