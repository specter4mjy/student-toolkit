package ml.mk.jm.ay.ak.studenttoolkit.database;

import android.provider.BaseColumns;

/**
 * Created by Marc.
 * This class was originally meant to be used to hold table data, but was not used in the end.
 * It is kept here in order to provide a place for other table definitions when added in the future.
 */
public class DatabaseContract {

    //A simple instructor
    public DatabaseContract(){    }

    //An inner class containing contants to be used as a table definition.
    //Shown below is the To-Do table definition.
    public static abstract class Database implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_TITLE_ID = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DUE = "due";
    }

}
