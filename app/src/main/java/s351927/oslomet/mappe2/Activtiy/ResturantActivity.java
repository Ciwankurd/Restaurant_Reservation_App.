package s351927.oslomet.mappe2.Activtiy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import s351927.oslomet.mappe2.DB.DatabaseHandler;
import s351927.oslomet.mappe2.Modul.Resturant;
import s351927.oslomet.mappe2.R;

public class ResturantActivity extends AppCompatActivity {
    private EditText innNavn;
    private EditText innTelefonnr;
    private EditText innAdresse;
    private EditText innType;
    private EditText innId;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturant_layout);
        innNavn=findViewById(R.id.Resturan_navn);
        innTelefonnr=findViewById(R.id.Resturant_Telefonnr);
        innAdresse=findViewById(R.id.Resturant_Adresse);
        innType=findViewById(R.id.Resturant_Type);
        innId=findViewById(R.id.Resturant_Id);
        db = new DatabaseHandler(this);
    }

    public void oppdater(View view) {
        if(!innNavn.getText().toString().isEmpty() && !innType.getText().toString().isEmpty() &&
                !innAdresse.getText().toString().isEmpty() && !innTelefonnr.getText().toString().isEmpty()
                && !innId.getText().toString().isEmpty()) {
            Resturant resturant = new Resturant();
            resturant.setNavn(innNavn.getText().toString());
            resturant.setTelefonnr(innTelefonnr.getText().toString());
            resturant.setAdresse(innAdresse.getText().toString());
            resturant.setType(innType.getText().toString());
            resturant.setID(Long.parseLong(innId.getText().toString()));
            db.OppdatereResturant(resturant);
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! Alle feltene må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void visalle(View view) {
        Intent d = new Intent(ResturantActivity.this,ResturantListActivity.class);
        startActivity(d);
    }

    public void slett(View view) {
        if(!innId.getText().toString().isEmpty()) {
            Long Resturantid = (Long.parseLong(innId.getText().toString()));
            db.SletteResturant(Resturantid);
            Log.d("slett", Resturantid + "");
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! ID felten må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void leggtil(View view) {
        if(!innNavn.getText().toString().isEmpty() && !innType.getText().toString().isEmpty() &&
                !innAdresse.getText().toString().isEmpty() && !innTelefonnr.getText().toString().isEmpty()) {
            Resturant resturant = new Resturant(innNavn.getText().toString(),
                    innTelefonnr.getText().toString(),
                    innAdresse.getText().toString(),
                    innType.getText().toString());
            db.RegistereRestrurant(resturant);
            Log.d("Legg inn: ", "legger til Resturanter");
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! Alle feltene må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
