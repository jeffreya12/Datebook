package cr.ac.itcr.datebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cr.ac.itcr.datebook.domain.User;

/**
 * Created by jefal on 19/02/2018.
 */

public class DatebookDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Datebook.db";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + DatebookContract.UserEntry.TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, "+ DatebookContract.UserEntry.COLUMN_NAME_USERNAME
                    +" TEXT UNIQUE NOT NULL, " + DatebookContract.UserEntry.COLUMN_NAME_PASSWORD
                    + " TEXT NOT NULL)";
    private static final String SQL_CREATE_EVENTS =
            "CREATE TABLE " + DatebookContract.EventEntry.TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, " + DatebookContract.EventEntry.COLUMN_NAME_NAME
                    + " TEXT NOT NULL, " + DatebookContract.EventEntry.COLUMN_NAME_DATE
                    + " TEXT NOT NULL, " + DatebookContract.EventEntry.COLUMN_NAME_PLACE
                    + " TEXT NOT NULL, " + DatebookContract.EventEntry.COLUMN_NAME_USERID
                    + " INTEGER NOT NULL)";

    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + DatebookContract.UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_EVENTS =
            "DROP TABLE IF EXISTS " + DatebookContract.EventEntry.TABLE_NAME;

    public DatebookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(SQL_DELETE_EVENTS);
        sqLiteDatabase.execSQL(SQL_DELETE_USERS);
        onCreate(sqLiteDatabase);

    }

    public long insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatebookContract.UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(DatebookContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());

        return db.insert(DatebookContract.UserEntry.TABLE_NAME, null, values);
    }

    public User getUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DatebookContract.UserEntry.TABLE_NAME, new String[]{"id",
                DatebookContract.UserEntry.COLUMN_NAME_USERNAME,
                DatebookContract.UserEntry.COLUMN_NAME_PASSWORD},
                DatebookContract.UserEntry.COLUMN_NAME_USERNAME + " =? ",
                new String[]{String.valueOf(username)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        User newUser = new User(cursor.getString(1), cursor.getString(2),
                Integer.parseInt(cursor.getString(0)));
        return newUser;
    }

}
