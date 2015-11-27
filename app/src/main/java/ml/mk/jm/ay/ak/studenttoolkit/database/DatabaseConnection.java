package ml.mk.jm.ay.ak.studenttoolkit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Marc.
 * This class is used to handle all database connections regarding the To-Do class.
 * At the moment it is tightly coupled with the To-Do class, but a DatabaseContract class exists to allow for
 * extensibility in the future.
 */
public final class DatabaseConnection extends SQLiteOpenHelper implements BaseColumns {

    //Several fields used to define database columns
    public static final String TABLE_NAME = "todo";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DUE = "due";

    //Miscellaneous database details
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "student_toolkit.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    //Fields used when retrieving database data
    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int DESCRIPTION = 2;
    public static final int DUE = 3;

    //Used to create SQL table for To-Do
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE  " + TABLE_NAME +" ("+ _ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_NAME_TITLE +" varchar(255), " + COLUMN_NAME_DESCRIPTION + " varchar(255), " + COLUMN_NAME_DUE  + " datetime(255))";

    //Deletes the To-Do table, if required
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS Todo";

    //Constructor for SQLiteOpenHelper subclass
    public DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //When created, make the To-Do table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    //Used to upgrade database using an actual database, new version number and old version number
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //When downgrading, use the upgrade method but replace the versions.
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
