package s351927.oslomet.mappe2.ContentProvider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class ResturantContentProvider extends ContentProvider {
    private static final String Resturant_ID="ID";
    private static final String Resturant_Navn="Navn";
    private static final String Resturant_telefonnr="Telefonnr";
    private static final String Resturant_Adresse="Adresse";
    private static final String Resturant_Type="Type";
    private static final String DB_NAME = "ResturantDB";
    private static final int DB_VERSJON = 1;
    private static final String TABLE = "Resturant";
    private static final String PROVIDER = "s351927.oslomet.mappe2";
    private static final int Resturant_id = 1;
    private static final int Resturant = 2;
    private SQLiteDatabase db;
    private String egenUri="content://s351927.oslomet.mappe2/Resturant";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/Resturant");
    private static final UriMatcher uriMatcher;
    ResturantContentProvider.DatabaseHelper DBhelper;

    public ResturantContentProvider() { }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "Resturant", Resturant);
        uriMatcher.addURI(PROVIDER, "Resturant/#", Resturant_id);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSJON);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE
                    + "(" + Resturant_ID + " INTEGER PRIMARY KEY," + Resturant_Navn + " TEXT," +
                    Resturant_telefonnr + " TEXT," + Resturant_Adresse+ " TEXT,"
                    + Resturant_Type+ " TEXT"+ ")";
            Log.d("Query Create Resturant",sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        DBhelper = new ResturantContentProvider.DatabaseHelper(getContext());
        db = DBhelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cur;
        if (uriMatcher.match(uri) == Resturant_id) {
            cur = db.query(TABLE, projection, Resturant_ID + "=" + uri.getPathSegments().get(1),
                    selectionArgs, null, null, sortOrder);
        } else {
            cur = db.query(TABLE, null, null,
                    null, null, null, null);
        }
        return cur;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == Resturant_id) { db.delete(TABLE, Resturant_ID + " = " +
                uri.getPathSegments().get(1), selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == Resturant) {
            db.delete(TABLE, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Resturant:
                return "vnd.android.cursor.dir/vnd.example.Resturant";
            case Resturant_id:
                return "vnd.android.cursor.item/vnd.example.Resturantid";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBhelper.getWritableDatabase();
        db.insert(TABLE, null, values);
        @SuppressLint("Recycle")
        Cursor c = db.query(TABLE, null, null,
                null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uriMatcher.match(uri) == Resturant_id) {
            db.update(TABLE, values, Resturant_ID + " = " +
                    uri.getPathSegments().get(1), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == Resturant) {
            db.update(TABLE, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}
