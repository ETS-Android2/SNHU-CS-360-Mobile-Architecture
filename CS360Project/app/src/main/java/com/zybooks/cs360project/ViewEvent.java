package com.zybooks.cs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewEvent extends AppCompatActivity {
    private EventDatabase mEventDb;
    private Event event;
    private TextView mTitle;
    private TextView mDate;
    private TextView mDesc;
    private int passedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Intent intent = getIntent();
        passedId = intent.getIntExtra("eventId", -1);
        mEventDb = EventDatabase.getInstance(getApplicationContext());

        mTitle = findViewById(R.id.viewEventTitle);
        mDate = findViewById(R.id.viewEventDate);
        mDesc = findViewById(R.id.viewEventDesc);
    }

    // Getting event data and setting to the views
    @Override
    protected void onResume() {
        super.onResume();
        event = mEventDb.getEvent(passedId);
        mTitle.setText(event.getTitle());
        mDate.setText(event.getFormattedDate());
        mDesc.setText(event.getDescription());
    }

    // When user presses edit button, start edit event activity and pass the event's id
    public void editEvent(View view) {
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("eventId", passedId);
        startActivity(intent);
    }

    // When user presses delete button, event is deleted
    public void deleteEvent(View view) {
        mEventDb.deleteEvent(event);
        finish();
    }

    // Simply finish current activity without doing anything
    public void goBack(View view) {
        finish();
    }
}