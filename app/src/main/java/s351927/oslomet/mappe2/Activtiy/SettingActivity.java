package s351927.oslomet.mappe2.Activtiy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import s351927.oslomet.mappe2.R;
import s351927.oslomet.mappe2.Fragmenter.SettingsPerferences;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        // bytte mellom mellom setting layout og prefernse.xml
        getSupportFragmentManager().beginTransaction().replace(R.id.setting_layout, new SettingsPerferences()).commit();

    }
}
