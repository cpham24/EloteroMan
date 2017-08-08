package edu.calstatela.cpham24.eloteroman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayLoginActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayMapActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayProfileActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplaySearchActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayVendorActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.user;

public class MainActivity extends AppCompatActivity {

    private final String TAG="loginActivity";
    Button loginBtn;
    EditText usernameET;
    String username;
    Bundle bundleForLoader;
    user current_user;
    private SharedPreferences userPrefs;
    String USER_PREFS="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_login);

        loginBtn=(Button)findViewById(R.id.login_button);
        usernameET=(EditText)findViewById(R.id.usernameET);
        final Context context = this;

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                username=usernameET.getText().toString();
                if(username.length()>1) {
                    Intent i = new Intent(context, DisplayMapActivity.class);
                    i.putExtra("username", username);
                    startActivity(i);
                }
                else
                    Toast.makeText(context, "Your name is blank",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
