package cr.ac.itcr.datebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cr.ac.itcr.datebook.domain.Event;
import cr.ac.itcr.datebook.domain.User;

/**
 * Created by jefal on 19/02/2018.
 */

public class DatebookDbHelper extends SQLiteOpenHelper {

    // Informacion de la base de datos
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Datebook.db";

    // Crea tabla se usuarios
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + DatebookContract.UserEntry.TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, "+ DatebookContract.UserEntry.COLUMN_NAME_USERNAME
                    +" TEXT UNIQUE NOT NULL, " + DatebookContract.UserEntry.COLUMN_NAME_PASSWORD
                    + " TEXT NOT NULL)";

    // Crea tabla de eventos
    private static final String SQL_CREATE_EVENTS =
            "CREATE TABLE " + DatebookContract.EventEntry.TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, " + DatebookContract.EventEntry.COLUMN_NAME_NAME
                    + " TEXT NOT NULL, " + DatebookContract.EventEntry.COLUMN_NAME_DATE
                    + " TEXT NOT NULL, " + DatebookContract.EventEntry.COLUMN_NAME_PLACE
                    + " TEXT NOT NULL, " + DatebookContract.EventEntry.COLUMN_NAME_USERID
                    + " INTEGER NOT NULL)";

    // Drop de la tabla de usuarios
    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + DatebookContract.UserEntry.TABLE_NAME;

    // Drop de la tabla de eventos
    private static final String SQL_DELETE_EVENTS =
            "DROP TABLE IF EXISTS " + DatebookContract.EventEntry.TABLE_NAME;

    // Constructor
    public DatebookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Ejecuta los querys de creacion de tablas
    // este metodo solo se ejecuta la primera vez que se abre la aplicacion
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS);

    }

    // Si la aplicacion detecta un cambio en la estructura base, borra las tablas y las
    // crea de nuevo
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(SQL_DELETE_EVENTS);
        sqLiteDatabase.execSQL(SQL_DELETE_USERS);
        onCreate(sqLiteDatabase);

    }

    // Metodo para insertar usuario en la base
    public long insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatebookContract.UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(DatebookContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());

        // Retorna el id insertado
        return db.insert(DatebookContract.UserEntry.TABLE_NAME, null, values);
    }

    // Metodo para insertar usuario en la base
    public long insertEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatebookContract.EventEntry.COLUMN_NAME_NAME, event.getName());
        values.put(DatebookContract.EventEntry.COLUMN_NAME_PLACE, event.getPlace());
        values.put(DatebookContract.EventEntry.COLUMN_NAME_DATE, event.getDate().toString());
        values.put(DatebookContract.EventEntry.COLUMN_NAME_USERID, event.getUserId());

        // Retorna el id insertado
        return db.insert(DatebookContract.EventEntry.TABLE_NAME, null, values);
    }

    // COnsulta usuario
    public User getUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        // Objeto encargado de recorrer los resultados de las consultas
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

        // Retorna el usuario consultado
        return newUser;
    }

    // Metodo para consultar el listado de eventos por usuario
    public List<Event> getAllEvents(int userId){
        List<Event> eventList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DatebookContract.EventEntry.TABLE_NAME + " WHERE "
                + DatebookContract.EventEntry.COLUMN_NAME_USERID + " = " + userId
                + " ORDER BY " + DatebookContract.EventEntry.COLUMN_NAME_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Se formatea el valor Date para poder manipularlo en Java
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        Date date = null;
        if(cursor.moveToFirst()){
            do {
                // Siempre que hayan entradas en la consulta,
                // recorra cada una y cree un objeto Evento
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setName(cursor.getString(1));

                try {
                    date = format.parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                event.setDate(date);

                event.setPlace(cursor.getString(3));
                event.setUserId(Integer.parseInt(cursor.getString(4)));

                eventList.add(event);
            } while (cursor.moveToNext()); // Siempre que hayan entradas
                                            // en el resultado de la consulta
        }

        // Retorna una lista con los eventos consultados
        return eventList;
    }

    // Borra un evento de la base de datos
    public void deleteEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatebookContract.EventEntry.TABLE_NAME, "id = ?", new String[]{
                String.valueOf(event.getId()) });
        db.close();
    }
}
