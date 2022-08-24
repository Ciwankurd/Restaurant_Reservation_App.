package s351927.oslomet.mappe2.Activtiy;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import s351927.oslomet.mappe2.DB.DatabaseHandler;
import s351927.oslomet.mappe2.Modul.Bestilling;
import s351927.oslomet.mappe2.Modul.Kontakt;
import s351927.oslomet.mappe2.Modul.Resturant;
import s351927.oslomet.mappe2.R;

public class VisBestillingList extends AppCompatActivity {
    LinearLayout ViseAlleBestillinger;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lister);
        ViseAlleBestillinger=findViewById(R.id.utskriftAlle_Resturant);
        db = new DatabaseHandler(this);
        StringBuilder tekst= new StringBuilder();
        List<Bestilling> bestillingList = db.HenteAlleBestilling();

        for (Bestilling bestilling : bestillingList) {
            List<Kontakt> kontaktList = db.DeltakelserInIvitsjon(bestilling.getID());
            Resturant resturant=db.BesstilletResturant(bestilling.getID());
            tekst.append("Id: ").append(bestilling.getID()).append("\nDato : ").
                    append(bestilling.getDate()).append(" \nTid : ").append(bestilling.getTid()).
                    append("\nResturant :\nNavn : ").append(resturant.getNavn()).append("\nTelefonr :")
                    .append(resturant.getAdresse()).append("\nAdresse : ").
                    append(resturant.getAdresse()).append("\n");
            for(Kontakt kontakt:kontaktList){
                tekst.append("Personer som invitert : \n" + "Navn : ").append(kontakt.getNavn()).append("\nTelefonnr :").append(kontakt.getTelefonnr()).append("\n-----------------------\n");
            }
            TextView Tb = new TextView(this);
            Tb.setText(tekst.toString());
            ViseAlleBestillinger.addView(Tb);
            Log.d("BestillingList", tekst.toString());
        }
    }
}