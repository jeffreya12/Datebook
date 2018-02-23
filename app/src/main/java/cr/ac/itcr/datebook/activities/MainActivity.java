package cr.ac.itcr.datebook.activities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cr.ac.itcr.datebook.R;
import cr.ac.itcr.datebook.database.DatebookDbHelper;
import cr.ac.itcr.datebook.domain.Event;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    private EditText filterText;
    private ArrayAdapter<Event> adapter;
    private DatebookDbHelper db;
    private AlertDialog.Builder builder;
    private SharedPreferences sessionPreferences;
    private List<Event> eventList;
    private ListView ltvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia del manejado de dabse de datos
        db = new DatebookDbHelper(this);

        // Creador de pop-ups
        builder = new AlertDialog.Builder(this);

        // Campo de busqueda
        filterText = (EditText)findViewById(R.id.main_etxSearch);

        // Datos de la sesion que comparten las actividades
        sessionPreferences = getApplicationContext().getSharedPreferences("SessionPreferences", 0); // 0 - for private mode
        final int userId = sessionPreferences.getInt("id", 0);

        // Boton flotante para crear eventos
        FloatingActionButton createEvent = findViewById(R.id.main_fabCreateEvent);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la actividad de crear eventos
                Intent myIntent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(myIntent);
            }
        });

        // Funcion que llena el ListView de eventos
        populateListView(userId);

        // Agrega la accion de cada evento
        ltvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Event event = adapter.getItem(i);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


                // Si se selecciona un evento, abre un pop-up
                // con la informacion y la opcion de borrar
                builder.setMessage(
                            "Name: " + event.getName() + "\n"
                            + "Place: " + event.getPlace() + "\n"
                            + "Date: " + formatter.format(event.getDate())
                        )
                        .setTitle(R.string.event_dialog_title)
                        .setNegativeButton(R.string.prompt_erase, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteEvent(event);
                                populateListView(userId);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Agrega la posibilidad de actualizar la lista conforme se realiza una busqueda
        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresca la lista de eventos despues de una modificacion
        sessionPreferences = getApplicationContext().getSharedPreferences("SessionPreferences", 0); // 0 - for private mode
        int userId = sessionPreferences.getInt("id", 0);
        populateListView(userId);
    }

    // Metodo que llena la lista de eventos
    private void populateListView(int userId){
        eventList = db.getAllEvents(userId);

        ltvEvents = findViewById(R.id.main_ltvEvents);

        // Objeto del cual se alimenta el ListView
        adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(eventList);
        ltvEvents.setAdapter(adapter);

    }

}
