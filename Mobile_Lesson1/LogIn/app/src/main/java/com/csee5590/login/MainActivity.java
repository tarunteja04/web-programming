package com.csee5590.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void validate(View v) {

        EditText usernameCtrl = (EditText) findViewById(R.id.username);
        EditText passwordCtrl = (EditText) findViewById(R.id.password);
        String userName = usernameCtrl.getText().toString();
        String password = passwordCtrl.getText().toString();

        boolean validationFlag = false;
        if (!userName.isEmpty() && !password.isEmpty()) {
            if (userName.equals("Admin") && password.equals("Admin")) {
                validationFlag = true;
            }
        }
        if (validationFlag) {
            Intent redirect = new Intent(MainActivity.this, Home.class);
            startActivity(redirect);
        }else
            {
                Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
