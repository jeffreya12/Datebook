package cr.ac.itcr.datebook.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cr.ac.itcr.datebook.R;
import cr.ac.itcr.datebook.database.DatebookDbHelper;
import cr.ac.itcr.datebook.domain.Event;

public class CreateEventActivity extends AppCompatActivity {

    private Spinner spnDay;
    private Spinner spnMonth;
    private Spinner spnYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        final DatebookDbHelper db = new DatebookDbHelper(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Los spinners se llenan con los arrays de datos en el xml values
        spnDay = findViewById(R.id.createevent_spnDay);
        spnMonth = findViewById(R.id.createevent_spnMonth);
        spnYear = findViewById(R.id.createevent_spnYear);

        // Se llena el spinner de dias
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDay.setAdapter(adapter);

        // Se llena el spinner de meses
        adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonth.setAdapter(adapter);

        // Se llena el spinner de annos
        adapter = ArrayAdapter.createFromResource(this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(adapter);

        // Boton de registro de eventos
        final Button btnSubmit = (Button) findViewById(R.id.createevent_btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtName = findViewById(R.id.createevent_etxName);
                EditText edtPlace = findViewById(R.id.createevent_etxPlace);

                String name = edtName.getText().toString();
                String place = edtPlace.getText().toString();

                String dateText = spnDay.getSelectedItem().toString() + "/" +
                                    spnMonth.getSelectedItem().toString() + "/" +
                                    spnYear.getSelectedItem().toString();

                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date date = null;
                try {
                    date = format.parse(dateText);
                } catch (ParseException e) {
                    e.printStackTrace();
                    builder.setMessage(R.string.error_date)
                            .setTitle(R.string.error_date_title);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                // Datos globales de sesion
                SharedPreferences sessionPreferences = getApplicationContext().getSharedPreferences("SessionPreferences", 0); // 0 - for private mode
                int userId = sessionPreferences.getInt("id", 0);

                Event newEvent = new Event(date, userId, place, name);

                // Se inserta el nuevo evento en la db
                long lastInsertId = db.insertEvent(newEvent);

                // Una vez insertado se finaliza la actividad
                finish();

            }
        });

    }
}
