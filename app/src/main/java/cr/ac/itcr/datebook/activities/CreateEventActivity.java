package cr.ac.itcr.datebook.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cr.ac.itcr.datebook.R;
import cr.ac.itcr.datebook.database.DatebookDbHelper;
import cr.ac.itcr.datebook.domain.Event;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        final DatebookDbHelper db = new DatebookDbHelper(this);

        final Button btnSubmit = (Button) findViewById(R.id.createevent_btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtName = findViewById(R.id.createevent_etxName);
                EditText edtPlace = findViewById(R.id.createevent_etxPlace);
                EditText edtDate = findViewById(R.id.createevent_edtDate);

                String name = edtName.getText().toString();
                String place = edtPlace.getText().toString();
                String dateText = edtDate.getText().toString();

                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date = null;
                try {
                    date = format.parse(dateText);
                    Log.d("input date: ", date.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SharedPreferences sessionPreferences = getApplicationContext().getSharedPreferences("SessionPreferences", 0); // 0 - for private mode

                int userId = sessionPreferences.getInt("id", 0);

                Event newEvent = new Event(date, userId, place, name);

                long lastInsertId = db.insertEvent(newEvent);
                Log.d("Last inserted id: ", String.valueOf(lastInsertId));

                finish();

            }
        });

    }
}
