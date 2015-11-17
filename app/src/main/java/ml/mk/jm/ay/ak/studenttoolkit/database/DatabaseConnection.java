package ml.mk.jm.ay.ak.studenttoolkit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Marc on 15/11/2015.
 */
public final class DatabaseConnection extends SQLiteOpenHelper implements BaseColumns {

    public static final String TABLE_NAME = "todo";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DUE = "due";


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "student_toolkit.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int DESCRIPTION = 2;
    public static final int DUE = 3;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE  " + TABLE_NAME +" ("+ _ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_NAME_TITLE +" varchar(255), " + COLUMN_NAME_DESCRIPTION + " varchar(255), " + COLUMN_NAME_DUE  + " datetime(255))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS Todo";

    public DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
