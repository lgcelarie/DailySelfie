package course.coursera.dailyselfie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Guillermo Celarie on 31/05/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SELFIES = "selfies";
    public static final String COLUMN_ID = "selfie_id";
    public static final String COLUMN_URI = "selfie_uri";
    public static final String COLUMN_DATE = "creation_date";

    private static final String DATABASE_NAME = "selfies.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SELFIES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_URI
            + " text not null," + COLUMN_DATE + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELFIES);
        onCreate(db);
    }

}
