package com.zybooks.cs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    private EventDatabase mEventDb;
    private String mUsername;
    private EditText mTitle;
    private EditText mYear;
    private EditText mMonth;
    private EditText mDay;
    private EditText mHour;
    private EditText mMinute;
    private EditText mDescription;
    private Button mAddButton;
    private Button mCancelButton;
    private String sYear;
    private String sMonth;
    private String sDay;
    private String sHour;
    private String sMinute;
    private int iYear;
    private int iMonth;
    private int iDay;
    private int iHour;
    private int iMinute;
    private Calendar mUserDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        mUsername = intent.getStringExtra("authUsername");
        mEventDb = EventDatabase.getInstance(getApplicationContext());

        mTitle = findViewById(R.id.eventTitle);
        mTitle.addTextChangedListener(new GenericTextWatcher(mTitle));
        mYear = findViewById(R.id.eventYear);
        mYear.addTextChangedListener(new GenericTextWatcher(mYear));
        mMonth = findViewById(R.id.eventMonth);
        mMonth.addTextChangedListener(new GenericTextWatcher(mMonth));
        mDay = findViewById(R.id.eventDay);
        mDay.addTextChangedListener(new GenericTextWatcher(mDay));
        mHour = findViewById(R.id.eventHour);
        mHour.addTextChangedListener(new GenericTextWatcher(mHour));
        mMinute = findViewById(R.id.eventMinute);
        mMinute.addTextChangedListener(new GenericTextWatcher(mMinute));
        mDescription = findViewById(R.id.eventDescription);
        mDescription.addTextChangedListener(new GenericTextWatcher(mDescription));
        mAddButton = findViewById(R.id.eventAddSubmit);
        mCancelButton = findViewById(R.id.eventCancelSubmit);

    }

    public void addEvent(View view) {
        if (!isEmpty(mTitle) && isValidDate(mYear, mMonth, mDay, mHour, mMinute) && !isEmpty(mDescription)) {
            Event event = new Event(mTitle.getText().toString(), mUserDate, mDescription.getText().toString(), mUsername);
            mEventDb.addEvent(event);
            finish();
        }
    }

    public void cancelAddEvent(View view) {
        finish();
    }

    public boolean isEmpty(EditText editText) {
        String eToString = editText.getText().toString();
        return eToString.matches("");
    }

    // Checking each numbers length of range
    public boolean isValidDate(EditText year, EditText month, EditText day, EditText hour, EditText minute) {
        Calendar systemTime = Calendar.getInstance();

        if (sYear.length() != 4) {
            Toast myToast = Toast.makeText(this, "Invalid year. (4 digits)", Toast.LENGTH_SHORT);
            myToast.show();
            return false;
        }
        if (sMonth.length() > 2 || sMonth.length() < 0 || iMonth > 12 || iMonth < 1) {
            Toast myToast = Toast.makeText(this, "Invalid month. (1 - 12)", Toast.LENGTH_SHORT);
            myToast.show();
            return false;
        }
        if (sDay.length() > 2 || sDay.length() < 0 || iDay > 31 || iDay < 1) {
            Toast myToast = Toast.makeText(this, "Invalid day. (1 - 31)", Toast.LENGTH_SHORT);
            myToast.show();
            return false;
        }
        if (sHour.length() > 2 || sHour.length() < 0 || iHour > 23 || iHour < 1) {
            Toast myToast = Toast.makeText(this, "Invalid hour. (1 - 23)", Toast.LENGTH_SHORT);
            myToast.show();
            return false;
        }
        if (sMinute.length() > 2 || sMinute.length() < 0 || iMinute > 59 || iMinute < 1) {
            Toast myToast = Toast.makeText(this, "Invalid minute. (1 - 59)", Toast.LENGTH_SHORT);
            myToast.show();
            return false;
        }

        Calendar userDate = Calendar.getInstance();
        userDate.set(iYear, iMonth, iDay, iHour, iMinute);
         // Checking if entered date is in the past
        if (userDate.getTimeInMillis() < systemTime.getTimeInMillis()) {
            Toast myToast = Toast.makeText(this, "Date cannot be before now.", Toast.LENGTH_SHORT);
            myToast.show();
            return false;
        }
        mUserDate = userDate;
        Toast myToast = Toast.makeText(this, "Event added: " + mTitle.getText().toString(), Toast.LENGTH_SHORT);
        myToast.show();
        return true;
    }

    private int convertStringtoInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(Exception e) {
            return 0;
        }
    }
    // Getting constant updates on fields
    private class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch(view.getId()) {
                case R.id.eventTitle:
                    mTitle = findViewById(R.id.eventTitle);
                    break;
                case R.id.eventYear:
                    mYear = findViewById(R.id.eventYear);
                    sYear = mYear.getText().toString();
                    iYear = convertStringtoInt(sYear);
                    break;
                case R.id.eventMonth:
                    mMonth = findViewById(R.id.eventMonth);
                    sMonth = mMonth.getText().toString();
                    iMonth = convertStringtoInt(sMonth);
                    break;
                case R.id.eventDay:
                    mDay = findViewById(R.id.eventDay);
                    sDay = mDay.getText().toString();
                    iDay = convertStringtoInt(sDay);
                    break;
                case R.id.eventHour:
                    mHour = findViewById(R.id.eventHour);
                    sHour = mHour.getText().toString();
                    iHour = convertStringtoInt(sHour);
                    break;
                case R.id.eventMinute:
                    mMinute = findViewById(R.id.eventMinute);
                    sMinute = mMinute.getText().toString();
                    iMinute = convertStringtoInt(sMinute);
                    break;
                case R.id.eventDescription:
                    mDescription = findViewById(R.id.eventDescription);
                    break;
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    }
}