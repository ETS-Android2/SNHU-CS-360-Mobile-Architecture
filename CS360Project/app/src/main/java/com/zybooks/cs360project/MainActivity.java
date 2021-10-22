/*
*   Program: Event Tracker
*   Author: Drew Townsend
*   Date: 10/16/21
*   Description: Android app that authenticates users or registers users, and tracks their events.
*                App can send SMS messages when the current day matches event day.
 */

package com.zybooks.cs360project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventRecyclerAdapter.ItemClickListener {

    private EventDatabase mEventDb;
    private String mUsername;
    private List<Event> mEvents;
    private User user;
    EventRecyclerAdapter adapter;
    final int REQUEST_SMS_CODE = 0;
    private String smsPermission = Manifest.permission.SEND_SMS;
    private Calendar sysTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        // Getting username from user
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("authUsername");

        // Welcome message
        Toast myToast = Toast.makeText(this, "Welcome, " + mUsername, Toast.LENGTH_SHORT);
        myToast.show();

        // Getting events under user in DB
        mEventDb = EventDatabase.getInstance(getApplicationContext());
        user = mEventDb.getUser(mUsername);
        mEvents = mEventDb.getEvents(mUsername);

        // Getting SMS permissions for device
        checkSMSPerms();
        smsPermission = Manifest.permission.SEND_SMS;

        // system time for checking if current date is equal to event dates, and if so send SMS
        sysTime = Calendar.getInstance();
        sendSMS();
    }

    // Open ViewEvent on selected event
    public void onItemClick(View view, int position, int eventId) {
        Toast.makeText(this, "You clicked ID: " + eventId + "on row number " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ViewEvent.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }

    // Open AddEvent on button press
    public void addEventClick(View view) {
        Intent intent = new Intent(this, AddEvent.class);
        intent.putExtra("authUsername", mUsername);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEvents = mEventDb.getEvents(mUsername);

        // Create recycler view
        RecyclerView recyclerView = findViewById(R.id.rcEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventRecyclerAdapter(this, mEvents);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // Getting user permissions for SMS message
    private void checkSMSPerms() {
        if (!user.smsPermAlreadyAsked()) {
            ActivityCompat.requestPermissions(this,
            new String[] { smsPermission }, REQUEST_SMS_CODE);
            user.setSmsBool(true);
        }
    }

    // Send an SMS if today's date lines up with event date.
    // Technically, this can't send an SMS as I couldn't find a way to do that via the emulator
    // But all of the functionality is there, and it instead sends a Toast notification
    public void sendSMS() {
        if (!mEvents.isEmpty()) {
            for (Event event : mEvents) {
                if (sysTime.get(Calendar.YEAR) == event.getDate().get(Calendar.YEAR) &&
                        sysTime.get(Calendar.MONTH) == event.getDate().get(Calendar.MONTH) &&
                        sysTime.get(Calendar.DAY_OF_MONTH) == event.getDate().get(Calendar.DAY_OF_MONTH)) {
                    if (ContextCompat.checkSelfPermission(this, smsPermission)
                            == PackageManager.PERMISSION_GRANTED) {
                        String message = "Your event: " + event.getTitle() + " is due today!";
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("1234567890", null, message, null, null);
                        Toast myToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
                        myToast.show();
                    }
                }
            }
        }
    }

}

