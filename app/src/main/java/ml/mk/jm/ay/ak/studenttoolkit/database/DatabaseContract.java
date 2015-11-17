package ml.mk.jm.ay.ak.studenttoolkit.database;

import android.provider.BaseColumns;

/**
 * Created by Marc on 15/11/2015.
 */
public class DatabaseContract {

    public DatabaseContract(){    }

    public static abstract class Database implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_TITLE_ID = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DUE = "due";
    }

}
