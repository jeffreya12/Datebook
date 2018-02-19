package cr.ac.itcr.datebook.database;

/**
 * Created by jefal on 19/02/2018.
 *
 * Clase con los nombres de los atributos de las tablas en la base de datos
 *
 */

public class DatebookContract {
    private DatebookContract(){}

    //Atributos de la tabla User
    public static class UserEntry{
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    //Atributos de la tabla Event
    public static class EventEntry{
        public static final String TABLE_NAME = "Event";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_USERID = "userId";
    }

}
