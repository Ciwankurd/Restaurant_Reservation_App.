package s351927.oslomet.mappe2.Activtiy;

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
import s351927.oslomet.mappe2.Modul.Kontakt;
import s351927.oslomet.mappe2.R;


public class PersonActivity extends AppCompatActivity {
    private EditText innNavn;
    private EditText innTelefonnr;
    private EditText innId;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_layout);
         innNavn = findViewById(R.id.Person_navn);
         innTelefonnr = findViewById(R.id.Person_Telefonnr);
         innId = findViewById(R.id.Person_Id);
        db = new DatabaseHandler(this);
    }

    public void PersonOppdater(View view) {
        if(!innNavn.getText().toString().isEmpty() && !innTelefonnr.getText().toString().isEmpty()
                && !innId.getText().toString().isEmpty()) {
            Kontakt kontakt = new Kontakt();
            kontakt.setNavn(innNavn.getText().toString());
            kontakt.setTelefonnr(innTelefonnr.getText().toString());
            kontakt.setID(Long.parseLong(innId.getText().toString()));
            db.OppdatereKontaktInfo(kontakt);
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! Alle feltene må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void visallePersoner(View view) {
        Intent d = new Intent(PersonActivity.this, PersonList.class);
        startActivity(d);
    }

    public void SlettPerson(View view) {
        if(!innId.getText().toString().isEmpty()) {
            Long Personid = (Long.parseLong(innId.getText().toString()));
            db.SletteKontakt(Personid);
            Log.d("slett", Personid + "");
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! ID felten må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void LeggtilPerson(View view) {
        if(!innNavn.getText().toString().isEmpty() && !innTelefonnr.getText().toString().isEmpty()) {
            Kontakt kontakt = new Kontakt(innNavn.getText().toString(), innTelefonnr.getText().toString());
            db.RegistereKontakter(kontakt);
            Log.d("Legg inn: ", "legger til kontakter");
        }
        else {
            Toast toast = Toast.makeText(this, "Feil! Navn og Telefonnr. feltene må fylles!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
