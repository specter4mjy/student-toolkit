package ml.mk.jm.ay.ak.studenttoolkit.map;

/**
 * Created by Ahmed.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ml.mk.jm.ay.ak.studenttoolkit.R;

import static android.util.Log.d;

//Class DatabaseHandler handle create the data base and fill it with all the information required.
public class DatabaseHandler extends SQLiteOpenHelper  {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "locationsManager";

    // Contacts table name
    private static final String TABLE_LOCATION = "locations";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ROOM = "roomNumber";
    private static final String KEY_LAT = "roomLat";
    private static final String KEY_LNG = "roomLng";
    //Contractor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //This method called by MapActivity class. When it run for the first time It invoke onCreate by getWritableDatabase(); to create the table, then it check
    //the there isn't any data in the table write all the data in.
    public void createDatabase(Context context) {
        SQLiteDatabase dB = this.getWritableDatabase(); //Invoke onCreate when it run for the first time lunch to create the data base tables.

        Cursor mCursor = dB.rawQuery("SELECT * FROM " + TABLE_LOCATION, null); //Gheck if the table been filled already.
        if (!mCursor.moveToNext())
        {
            //Writing information from res/locationData.xml file to the data base.
            for (int i = 1; i <= 89; i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_ROOM, context.getString(R.string.roomNumber + i));// Getting room number
                values.put(KEY_LAT, context.getString(R.string.roomLat + i)); // Getting room Lat
                values.put(KEY_LNG, context.getString(R.string.roomLng + i)); // Getting room Lng
                // Inserting Row
                dB.insert(TABLE_LOCATION, null, values);
            }
        }
        this.close();
    }

    @Override // Creating table.
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ROOM + " TEXT," + KEY_LAT + " TEXT,"
                + KEY_LNG + " TEXT" + ");";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override // Upgrading database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Get room location
    Location getLocation(String roomNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATION, new String[]{KEY_ID,
                        KEY_ROOM, KEY_LAT, KEY_LNG}, KEY_ROOM + "=?",
                new String[]{roomNumber}, null, null, null, null);
        if (!cursor.moveToFirst()){
            return null; //If room location not available in the app data base.
        }else{
            cursor.moveToFirst();
            Location location = new Location(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3));
            // Return contact
            d("get contact", location.room_lat);
            return location;
        }
    }
}