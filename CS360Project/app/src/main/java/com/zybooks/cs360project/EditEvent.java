package com.zybooks.cs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditEvent extends AppCompatActivity {

    private EventDatabase mEventDb;
    private Event mEvent;
    private int mPassedId;
    private EditText mTitle;
    private EditText mYear;
    private EditText mMonth;
    private EditText mDay;
    private EditText mHour;
    private EditText mMinute;
    private EditText mDescription;
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
        setContentView(R.layout.activity_edit_event);

        Intent intent = getIntent();
        mPassedId = intent.getIntExtra("eventId", -1);
        mEventDb = EventDatabase.getInstance(getApplicationContext());
        mEvent = mEventDb.getEvent(mPassedId);

        mTitle = findViewById(R.id.eventTitle);
        mTitle.setText(mEvent.getTitle());
        mTitle.addTextChangedListener(new EditEvent.GenericTextWatcher(mTitle));
        mYear = findViewById(R.id.eventYear);
        mYear.setText(Integer.toString(mEvent.getDate().get(Calendar.YEAR)));
        mYear.addTextChangedListener(new EditEvent.GenericTextWatcher(mYear));
        mMonth = findViewById(R.id.eventMonth);
        mMonth.setText(Integer.toString(mEvent.getDate().get(Calendar.MONTH) + 1));
        mMonth.addTextChangedListener(new EditEvent.GenericTextWatcher(mMonth));
        mDay = findViewById(R.id.eventDay);
        mDay.setText(Integer.toString(mEvent.getDate().get(Calendar.DAY_OF_MONTH)));
        mDay.addTextChangedListener(new EditEvent.GenericTextWatcher(mDay));
        mHour = findViewById(R.id.eventHour);
        mHour.setText(Integer.toString(mEvent.getDate().get(Calendar.HOUR_OF_DAY)));
        mHour.addTextChangedListener(new EditEvent.GenericTextWatcher(mHour));
        mMinute = findViewById(R.id.eventMinute);
        mMinute.setText(Integer.toString(mEvent.getDate().get(Calendar.MINUTE)));
        mMinute.addTextChangedListener(new EditEvent.GenericTextWatcher(mMinute));
        mDescription = findViewById(R.id.eventDescription);
        mDescription.setText(mEvent.getDescription());
        mDescription.addTextChangedListener(new EditEvent.GenericTextWatcher(mDescription));
        sYear = mYear.getText().toString();
        iYear = convertStringToInt(sYear);
        sMonth = mMonth.getText().toString();
        iMonth = convertStringToInt(sMonth);
        sDay = mDay.getText().toString();
        iDay = convertStringToInt(sDay);
        sHour = mHour.getText().toString();
        iHour = convertStringToInt(sHour);
        sMinute = mMinute.getText().toString();
        iMinute = convertStringToInt(sMinute);
    }

    public void submitUpdate(View view) {
        if (!isEmpty(mTitle) && isValidDate(mYear, mMonth, mDay, mHour, mMinute) && !isEmpty(mDescription)) {
            Event event = new Event(mPassedId, mTitle.getText().toString(), mUserDate,
                    mDescription.getText().toString(), mEvent.getEventusername());
            if (!mEventDb.updateEvent(event)) {
                Toast myToast = Toast.makeText(this, "An error occurred with the update...", Toast.LENGTH_SHORT);
                myToast.show();
            }
            finish();
        }
    }

    public void cancelUpdate(View view) {
        finish();
    }

    public boolean isEmpty(EditText editText) {
        String eToString = editText.getText().toString();
        return eToString.matches("");
    }

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

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.eventTitle:
                    mTitle = findViewById(R.id.eventTitle);
                    break;
                case R.id.eventYear:
                    mYear = findViewById(R.id.eventYear);
                    sYear = mYear.getText().toString();
                    iYear = convertStringToInt(sYear);
                    break;
                case R.id.eventMonth:
                    mMonth = findViewById(R.id.eventMonth);
                    sMonth = mMonth.getText().toString();
                    iMonth = convertStringToInt(sMonth);
                    break;
                case R.id.eventDay:
                    mDay = findViewById(R.id.eventDay);
                    sDay = mDay.getText().toString();
                    iDay = convertStringToInt(sDay);
                    break;
                case R.id.eventHour:
                    mHour = findViewById(R.id.eventHour);
                    sHour = mHour.getText().toString();
                    iHour = convertStringToInt(sHour);
                    break;
                case R.id.eventMinute:
                    mMinute = findViewById(R.id.eventMinute);
                    sMinute = mMinute.getText().toString();
                    iMinute = convertStringToInt(sMinute);
                    break;
                case R.id.eventDescription:
                    mDescription = findViewById(R.id.eventDescription);
                    break;
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    }

    private int convertStringToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(Exception e) {
            return 0;
        }
    }
}