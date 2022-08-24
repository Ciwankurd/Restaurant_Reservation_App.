package s351927.oslomet.mappe2.Activtiy;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import s351927.oslomet.mappe2.DB.DatabaseHandler;
import s351927.oslomet.mappe2.Modul.Resturant;
import s351927.oslomet.mappe2.R;

public class ResturantListActivity extends AppCompatActivity {
     LinearLayout ViseAlleResturant;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lister);
        ViseAlleResturant=findViewById(R.id.utskriftAlle_Resturant);
        db = new DatabaseHandler(this);
        String tekst;
        List<Resturant> resturantList = db.HenteAlleResturant();
        for (Resturant resturant : resturantList) {
            tekst ="Id: " + resturant.getID() + "\nNavn: " +
                    resturant.getNavn() + " \nTelefon: " +
                    resturant.getTelefonnr()+"\nAdresse: "+
                    resturant.getAdresse()+"\nType: "+resturant.getType()+"\n-----------------------";
            TextView Tb = new TextView(this);
            Tb.setText(tekst);
            ViseAlleResturant.addView(Tb);
            Log.d("Navn: ", tekst);
        }

    }
}
