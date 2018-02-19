package cr.ac.itcr.datebook.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cr.ac.itcr.datebook.R;
import cr.ac.itcr.datebook.database.DatebookDbHelper;
import cr.ac.itcr.datebook.domain.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instancia de la clase que maneja la base de datos
        final DatebookDbHelper db = new DatebookDbHelper(this);

        // Objeto para crear alertas
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Boton de login
        Button btnSignOn = (Button)findViewById(R.id.login_btnSignOn);
        btnSignOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //instancia de la caja de texto
                EditText etxUsername = (EditText)findViewById(R.id.login_etxUsername);
                EditText etxPassword = (EditText)findViewById(R.id.login_etxPassword);
                String username = etxUsername.getText().toString();
                String password = etxPassword.getText().toString();

                User user = new User(username, password);

                // Inserta el nuevo usuario
                long id = db.insertUser(user);

                // En caso de que exista el usuario, se muestra un dialogo con el error
                if(id == -1){
                    builder.setMessage(R.string.error_invalid_username)
                            .setTitle(R.string.error_invalid_sign_on);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                // Se muestra un dialogo de registro exitoso
                else {
                    builder.setMessage(R.string.alert_valid_sign_on)
                            .setTitle(R.string.alert_valid_sign_on_body);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });


        //Boton de registro
        Button btnSignIn = (Button)findViewById(R.id.login_btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instancia de la caja de texto
                EditText etxUsername = (EditText)findViewById(R.id.login_etxUsername);
                EditText etxPassword = (EditText)findViewById(R.id.login_etxPassword);
                String username = etxUsername.getText().toString();
                String password = etxPassword.getText().toString();

                // Maneja la excepcion en caso de que el usuario no exista en la base de datos
                try{
                    User retrievedUser = db.getUser(username);

                    // COnfirma que la contrasena sea igual a la registrada
                    if(password.equals(retrievedUser.getPassword())){
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    }

                    //Si las contrasenas no concuerdan, muestra una alerta con el error
                    else{
                        builder.setMessage(R.string.error_invalid_sign_in)
                                .setTitle(R.string.error_sign_in);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }

                // Si no existe el usuario, muestra un dialogo
                catch(Exception e){
                    builder.setMessage(R.string.error_invalid_sign_in)
                            .setTitle(R.string.error_username);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }


}

