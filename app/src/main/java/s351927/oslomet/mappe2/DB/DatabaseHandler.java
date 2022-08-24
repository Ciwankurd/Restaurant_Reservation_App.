package s351927.oslomet.mappe2.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import s351927.oslomet.mappe2.Modul.Bestilling;
import s351927.oslomet.mappe2.Modul.Kontakt;
import s351927.oslomet.mappe2.Modul.Resturant;

public class DatabaseHandler extends SQLiteOpenHelper {
   // Datebase Navn
   private  static final String DATABSE_NAVN="PersonMoteDB";
   // Database Version
    private static final int DB_VERSION =1;
    // Tabeller
    private static final String TABLE_KONTAKT="Kontakt";
    private static final String TABLE_Bestilling="Bestilling";
    private static final String TABLE_Resturant="Resturant";
    //hjelpe Table
    private static final String TABLE_Deltakelser ="Invitasjon";
    // Kontakt koloner
    private static final String Kontatk_ID="ID";
    private static final String Kontakt_Navn="Navn";
    private static final String Kontakt_telefonnr="Telefonnr";
    // Resrurant koloner
    private static final String Resturant_ID="ID";
    private static final String Resturant_Navn="Navn";
    private static final String Resturant_telefonnr="Telefonnr";
    private static final String Resturant_Adresse="Adresse";
    private static final String Resturant_Type="Type";
    // Bestilling koloner
    private static final String Bestilling_ID="B_ID";
    private static final String Bestilling_Dato="Dato";
    private static final String Bestilling_Tid="Tid";

    // Stringer for lage Tabller
    // Kontakt
    private static final String CREATE_TABLE_KONTAK="CREATE TABLE "+
            TABLE_KONTAKT+"("+Kontatk_ID+" INTEGER PRIMARY KEY,"+Kontakt_Navn+" TEXT,"+
            Kontakt_telefonnr+" TEXT"+")";
    //Resturnat
    private static final String CREATE_TABLE_RESTURRANT="CREATE TABLE "+
            TABLE_Resturant+"("+Resturant_ID+" INTEGER PRIMARY KEY,"+Resturant_Navn+" TEXT,"
            +Resturant_telefonnr+" TEXT,"+Resturant_Adresse+" TEXT,"+
            Resturant_Type+" TEXT"+")";
    //Bestilling
    private static final String CREATE_TABLE_BESTILLING="CREATE TABLE "+
            TABLE_Bestilling+"("+Bestilling_ID+" INTEGER PRIMARY KEY,"+Bestilling_Dato+" DATETIME,"+
            Bestilling_Tid + " TEXT," +Resturant_ID+" INTEGER,"+" FOREIGN KEY("+Resturant_ID+") REFERENCES "+
            TABLE_Resturant+"("+Resturant_ID+")"+")";
    //Deltakelser
    private static final String CREATE_TABLE_DELTAKELSER ="CREATE TABLE "+
            TABLE_Deltakelser +"("+Bestilling_ID+" INTEGER,"+Kontatk_ID+" INTEGER,"
            +" FOREIGN KEY("+Bestilling_ID+")REFERENCES "+
            TABLE_Bestilling+",FOREIGN KEY("+Kontatk_ID+")REFERENCES "+
            TABLE_KONTAKT+",PRIMARY KEY("+Bestilling_ID+","+Kontatk_ID+"))";
    public DatabaseHandler(Context context) {
        super(context, DATABSE_NAVN, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Lage Tabeller
        db.execSQL(CREATE_TABLE_KONTAK);
        Log.d("CREATE_TABLE_KONTAK",CREATE_TABLE_KONTAK);
        db.execSQL(CREATE_TABLE_BESTILLING);
        Log.d("CREATE_TABLE_BESTILLING",CREATE_TABLE_BESTILLING);
        db.execSQL(CREATE_TABLE_RESTURRANT);
        Log.d("CREATE_TABLE_RESTURRANT",CREATE_TABLE_RESTURRANT);
        db.execSQL(CREATE_TABLE_DELTAKELSER);
        Log.d("CREATE_TABLE_DELTAKELSER",CREATE_TABLE_DELTAKELSER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // slette gamle tabler for ny OnUpgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Resturant);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Bestilling);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Deltakelser);
        // sette tabller på nytt
        onCreate(db);
    }

    //---------------------------------Resturant-Handling-----------------------------

    //Regestere en Resturant
    public void RegistereRestrurant(Resturant resturant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Resturant_Navn, resturant.getNavn());
        values.put(Resturant_telefonnr, resturant.getTelefonnr());
        values.put(Resturant_Adresse, resturant.getAdresse());
        values.put(Resturant_Type, resturant.getType());
        // insert row
        db.insert(TABLE_Resturant, null, values);
        db.close();
        Log.d("RegisterResturan",resturant.getNavn()+
                " "+resturant.getTelefonnr()
                +" "+resturant.getAdresse()
                +" "+resturant.getType());
    }
    // Hente Alle Restunanter
    @SuppressLint("Range")
    public List<Resturant> HenteAlleResturant() {
        List<Resturant> resturant = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_Resturant;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(selectQuery, null);

        //legge til Listen
        if (c.moveToFirst()) {
            do {
                Resturant resturant1 = new Resturant();
                resturant1.setID(c.getLong(c.getColumnIndex(Resturant_ID)));
                resturant1.setNavn(c.getString(c.getColumnIndex(Resturant_Navn)));
                resturant1.setTelefonnr(c.getString(c.getColumnIndex(Resturant_telefonnr)));
                resturant1.setAdresse(c.getString(c.getColumnIndex(Resturant_Adresse)));
                resturant1.setType(c.getString(c.getColumnIndex(Resturant_Type)));

                resturant.add(resturant1);
            }
            while (c.moveToNext());
            c.close();
            db.close();
        }

        return resturant;
    }

    // Oppdater en Resturant
    public void OppdatereResturant(Resturant resturant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Resturant_Navn, resturant.getNavn());
        values.put(Resturant_telefonnr, resturant.getTelefonnr());
        values.put(Resturant_Adresse, resturant.getAdresse());
        values.put(Resturant_Type, resturant.getType());
        // Oppdatere raden
        db.update(TABLE_Resturant, values, Resturant_ID + " = ?",
                new String[]{String.valueOf(resturant.getID())});
        db.close();

    }

    //Slette en person
    public void SletteResturant(Long resturant_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Resturant, Resturant_ID + " = ?",
                new String[]{String.valueOf(resturant_id)});
        db.close();
    }

//---------------------------------Kontakter-Handling----------------------------

//Regestere en Kontakter
public void RegistereKontakter(Kontakt kontakt) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Kontakt_Navn, kontakt.getNavn());
    values.put(Kontakt_telefonnr, kontakt.getTelefonnr());
    // insert row
    db.insert(TABLE_KONTAKT, null, values);
    db.close();
    Log.d("registerKontak",kontakt.getNavn()+" "+kontakt.getTelefonnr());

}
    // Hente Alle Kontakter
    @SuppressLint("Range")
    public List<Kontakt> HenteAlleKontakter() {
        List<Kontakt> kontaktList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_KONTAKT;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(selectQuery, null);

        //legge til Listen
        if (c.moveToFirst()) {
            do {
                Kontakt Kontakt1 = new Kontakt();
                Kontakt1.setID(c.getLong(c.getColumnIndex(Kontatk_ID)));
                Kontakt1.setNavn(c.getString(c.getColumnIndex(Kontakt_Navn)));
                Kontakt1.setTelefonnr(c.getString(c.getColumnIndex(Kontakt_telefonnr)));
                kontaktList.add(Kontakt1);
            }
            while (c.moveToNext());
            c.close();
            db.close();
        }

        return kontaktList;
    }

    // Oppdater en kontaktinfo
    public void OppdatereKontaktInfo(Kontakt kontakt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Kontakt_Navn, kontakt.getNavn());
        values.put(Kontakt_telefonnr, kontakt.getTelefonnr());
        // Oppdatere raden
        db.update(TABLE_KONTAKT, values, Kontatk_ID + " = ?",
                new String[]{String.valueOf(kontakt.getID())});
        db.close();

    }

    //Slette en person
    public void SletteKontakt(Long Kontakt_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KONTAKT, Kontatk_ID + " = ?",
                new String[]{String.valueOf(Kontakt_id)});
        db.close();
    }
    //---------------------------------Bestilling-Handling----------------------------

   // Lage en Bestilling
    public void RegisterBestilling(Bestilling bestilling, Long resturant_id, ArrayList<Long> kontakt_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Bestilling_Dato, bestilling.getDate());
        values.put(Bestilling_Tid, bestilling.getTid());
        values.put(Resturant_ID,resturant_id);

        // insert row
        Long bestilling_id =  db.insert(TABLE_Bestilling, null, values);
        // lage invitasjon for hver venner
        for(Long id:kontakt_id){
            createInvitasjon(bestilling_id,id);
        }
        db.close();
        Log.d("RegBestilling",bestilling.getDate()+
                bestilling.getTid()+resturant_id);
    }
    /*
    // Oppdater en Bestillinginfo
    public void OppdatereBestilling(Bestilling bestilling) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Bestilling_Dato,bestilling.getDate());
        values.put(Bestilling_Tid,bestilling.getTid());
        // Oppdatere raden
        db.update(TABLE_Bestilling, values, Bestilling_ID + " = ?",
                new String[]{String.valueOf(bestilling.getID())});
        db.close();
    }

    //Slette en Bestilling
    public void SletteBestilling(Long bestiling_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Bestilling, Bestilling_ID + " = ?",
                new String[]{String.valueOf(bestiling_id)});
        db.close();
    }

     */
    // Vise_Alle_Bestilling
    @SuppressLint("Range")
    public List<Bestilling> HenteAlleBestilling() {
        List<Bestilling> BestillingList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_Bestilling;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(selectQuery, null);

        //legge til Listen
        if (c.moveToFirst()) {
            do {
                Bestilling bestilling1 = new Bestilling();
                bestilling1.setID(c.getLong(c.getColumnIndex(Bestilling_ID)));
                bestilling1.setDate(c.getString(c.getColumnIndex(Bestilling_Dato)));
                bestilling1.setTid(c.getString(c.getColumnIndex(Bestilling_Tid)));
                BestillingList.add(bestilling1);
            }
            while (c.moveToNext());
            c.close();
            db.close();
        }

        return BestillingList;
    }
    //---------------------------------Deltakelse-Handling----------------------------
    public void createInvitasjon(Long bestilling_id, Long Kontakt_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Bestilling_ID, bestilling_id);
        values.put(Kontatk_ID, Kontakt_id);
        db.insert(TABLE_Deltakelser, null, values);
        db.close();
        Log.d("RegBestilling",bestilling_id+" "+bestilling_id);

    }
    //Hent alle Deltakelse som blir invitert
    @SuppressLint("Range")
    public List<Kontakt> DeltakelserInIvitsjon(Long BestId) {
        List<Kontakt> kontaktList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_KONTAKT + " , " + TABLE_Bestilling + " , " +
                TABLE_Deltakelser+ " WHERE " + TABLE_Bestilling + "." +
                Bestilling_ID + "= '" + BestId + "'" + " AND " + TABLE_KONTAKT+ "." + Kontatk_ID + " = " +
                TABLE_Deltakelser + "." + Kontatk_ID + " AND " + TABLE_Bestilling + "." +Bestilling_ID + " =" +
                TABLE_Deltakelser + "." + Bestilling_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Kontakt Kontakt1 = new Kontakt();
                Kontakt1.setID(c.getLong(c.getColumnIndex(Kontatk_ID)));
                Kontakt1.setNavn(c.getString(c.getColumnIndex(Kontakt_Navn)));
                Kontakt1.setTelefonnr(c.getString(c.getColumnIndex(Kontakt_telefonnr)));
                kontaktList.add(Kontakt1);
            } while (c.moveToNext());
            c.close();
            db.close();
        }

        return kontaktList;
    }
     // Hente Resturant som bli bestillet
    @SuppressLint("Range")
    public Resturant BesstilletResturant(Long bestId) {
        Resturant resturant = new Resturant();
        String selectQuery = "SELECT " + TABLE_Resturant + ".* FROM " + TABLE_Bestilling + " JOIN " + TABLE_Resturant +
                " ON " + TABLE_Bestilling + "." + Resturant_ID + " = " + TABLE_Resturant + "." + Resturant_ID
                + " WHERE " + TABLE_Bestilling + "." + Bestilling_ID + " = " + bestId;
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("BestilletResturant", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                resturant.setID(c.getLong(c.getColumnIndex(Resturant_ID)));
                resturant.setNavn(c.getString(c.getColumnIndex(Resturant_Navn)));
                resturant.setTelefonnr(c.getString(c.getColumnIndex(Resturant_telefonnr)));
                resturant.setAdresse(c.getString(c.getColumnIndex(Resturant_Adresse)));
                resturant.setType(c.getString(c.getColumnIndex(Resturant_Type)));
            } while (c.moveToNext());
            c.close();
            db.close();

        }
        return resturant;
    }
}
