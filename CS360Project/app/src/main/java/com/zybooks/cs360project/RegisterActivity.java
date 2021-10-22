package com.zybooks.cs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EventDatabase mEventDb;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mNameNotUnique;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.registerUsernameField);
        mPassword = findViewById(R.id.registerPasswordField);
        mNameNotUnique = findViewById(R.id.registerNameNotUnique);
        mRegisterButton = findViewById(R.id.registerMainButton);

        mEventDb = EventDatabase.getInstance(getApplicationContext());
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                regUser();
            }
        });
    }

    // Ensuring name is unique and sending to DB
    public void regUser() {
        User user = new User(mUsername.getText().toString(), mPassword.getText().toString());
        if (!mEventDb.registerUser(user)) {
            mNameNotUnique.setVisibility(View.VISIBLE);
        } else {
            mNameNotUnique.setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.putExtra("regUsername", user.getUsername());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}