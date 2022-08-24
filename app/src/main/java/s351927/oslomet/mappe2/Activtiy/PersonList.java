package s351927.oslomet.mappe2.Activtiy;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import s351927.oslomet.mappe2.DB.DatabaseHandler;
import s351927.oslomet.mappe2.Modul.Kontakt;
import s351927.oslomet.mappe2.R;

public class PersonList extends AppCompatActivity {
    LinearLayout ViseAllePersoner;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lister);
        ViseAllePersoner=findViewById(R.id.utskriftAlle_Resturant);
        db = new DatabaseHandler(this);
        String tekst;
        List<Kontakt> kontakter = db.HenteAlleKontakter();
        for (Kontakt kontakt : kontakter) {
            tekst ="Id: " + kontakt.getID() + "\nNavn: " +
                    kontakt.getNavn() + " \nTelefon: " +
                    kontakt.getTelefonnr()+"\n-----------------------";
            TextView Tb = new TextView(this);
            Tb.setText(tekst);
            ViseAllePersoner.addView(Tb);
            Log.d("Navn: ", tekst);
        }
    }
}
