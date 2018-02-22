package cr.ac.itcr.datebook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cr.ac.itcr.datebook.R;
import cr.ac.itcr.datebook.database.DatebookDbHelper;
import cr.ac.itcr.datebook.domain.Event;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatebookDbHelper db = new DatebookDbHelper(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        SharedPreferences sessionPreferences = getApplicationContext().getSharedPreferences("SessionPreferences", 0); // 0 - for private mode
        int userId = sessionPreferences.getInt("id", 0);

        FloatingActionButton createEvent = findViewById(R.id.main_fabCreateEvent);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(myIntent);
            }
        });

        final List<Event> eventList = db.getAllEvents(userId);

        ListView ltvEvents = findViewById(R.id.main_ltvEvents);

        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(eventList);
        ltvEvents.setAdapter(adapter);

        ltvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                String eventId = String.valueOf(eventList.get(i).getId());
                intent.putExtra("eventId", eventId);
                startActivity(intent);
                */

                final Event event = eventList.get(i);

                builder.setMessage(
                            "Name: " + event.getName() + "\n"
                            + "Place: " + event.getPlace() + "\n"
                            + "Date: " + event.getDate()
                        )
                        .setTitle(R.string.event_dialog_title)
                        .setNegativeButton(R.string.prompt_erase, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteEvent(event);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    /*
    @Override
    public void onResume()
    {
        super.onResume();

    }
    */
}
