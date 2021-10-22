package com.zybooks.cs360project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EventDatabase mEventDb;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mAuthError;
    private Button mLoginButton;
    private Button mRegisterButton;
    private String regName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.usernameField);
        mPassword = findViewById(R.id.passwordField);
        mAuthError = findViewById(R.id.warningText);
        mLoginButton = findViewById(R.id.loginButton);
        mRegisterButton = findViewById(R.id.registerButton);

        ActivityResultLauncher<Intent> startRegistration = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            regName = data.getStringExtra("regUsername");
                            if (regName != null) {
                                closeActivity(regName);
                            }
                        }
                    }
                }
        );

        // For this small of a db, and due to the classroom setting I'm hoping it's okay that the
        // passwords aren't encrypted lol. And that they are returned with the usernames.
        mLoginButton.setOnClickListener(v -> authUser());

        // Open Register activity if Register button press
        mRegisterButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startRegistration.launch(intent);
        });
        mEventDb = EventDatabase.getInstance(getApplicationContext());
    }

    // Checking if user password is correct via DB. If so,
    public void authUser() {
        boolean authenticated;

        User user = new User(mUsername.getText().toString(), mPassword.getText().toString());
        authenticated = mEventDb.authenticateUser(user);

        if (!authenticated) {
            mAuthError.setVisibility(View.VISIBLE);
        } else {
            mAuthError.setVisibility(View.INVISIBLE);
            closeActivity(user.getUsername());
        }
    }

    // Closes activity
    public void closeActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("authUsername", username);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginButton.setOnClickListener(null);
        mRegisterButton.setOnClickListener(null);
    }
}