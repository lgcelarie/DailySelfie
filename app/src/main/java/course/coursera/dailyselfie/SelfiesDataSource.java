package course.coursera.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillermo Celarie on 31/05/2015.
 */
public class SelfiesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_URI, MySQLiteHelper.COLUMN_DATE };

    public SelfiesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Selfie createSelfie(String uri, String date) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_URI, uri);
        values.put(MySQLiteHelper.COLUMN_DATE, date);
        long insertId = database.insert(MySQLiteHelper.TABLE_SELFIES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SELFIES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Selfie newSelfie = cursorToSelfie(cursor);
        cursor.close();
        return newSelfie;
    }

    public void deleteSelfie(Selfie selfie) {
        long id = selfie.getId();
        System.out.println("Selfie deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_SELFIES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Selfie> getAllSelfies() {
        List<Selfie> comments = new ArrayList<Selfie>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SELFIES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Selfie selfie = cursorToSelfie(cursor);
            comments.add(selfie);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Selfie cursorToSelfie(Cursor cursor) {
        Selfie selfie = new Selfie();
        selfie.setId(cursor.getLong(0));
        selfie.setUri(cursor.getString(1));
        selfie.setDate(cursor.getString(2));

        return selfie;
    }
}


