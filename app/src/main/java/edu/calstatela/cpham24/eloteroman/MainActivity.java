package edu.calstatela.cpham24.eloteroman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayLoginActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayMapActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayProfileActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplaySettingsActivity;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.DisplayVendorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // we don't need to store a reference of these in the main class
        Button loginBtn    = (Button) findViewById(R.id.loginBtn);
        Button profileBtn  = (Button) findViewById(R.id.profileBtn);
        Button mapBtn      = (Button) findViewById(R.id.mapBtn);
        Button vendorBtn   = (Button) findViewById(R.id.vendorBtn);
        Button settingsBtn = (Button) findViewById(R.id.settingsBtn);

        // get a reference to the main context
        final Context main = this;

        // the following code set click listeners to each of the buttons
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main, DisplayLoginActivity.class);
                startActivity(i);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main, DisplayProfileActivity.class);
                startActivity(i);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main, DisplayMapActivity.class);
                startActivity(i);
            }
        });

        vendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main, DisplayVendorActivity.class);
                startActivity(i);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main, DisplaySettingsActivity.class);
                startActivity(i);
            }
        });
    }
}
