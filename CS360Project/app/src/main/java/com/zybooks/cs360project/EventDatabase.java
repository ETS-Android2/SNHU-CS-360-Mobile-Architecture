// I don't even want to talk about all of the casts and parses below...

package com.zybooks.cs360project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "events.db";
    private static final int VERSION = 1;

    private static EventDatabase mEventDb;

    public static EventDatabase getInstance(Context context) {
        if (mEventDb == null) {
            mEventDb = new EventDatabase(context);
        }
        return mEventDb;
    }

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_SMS_BOOL = "smsbool";
    }

    private static final class EventTable {
        private static final String TABLE = "events";
        private static final String COL_ID = "_id";
        private static final String COL_TITLE = "title";
        private static final String COL_DATE = "date"; // stored as Epoch for simplicity when ordering by date
        private static final String COL_DESCRIPTION = "description";
        private static final String COL_EVENT_USERNAME = "eventusername";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating user table
        db.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.COL_USERNAME + " primary key, " +
                UserTable.COL_PASSWORD + " text, " +
                UserTable.COL_SMS_BOOL + " integer)");

        // Creating event table
        db.execSQL("create table " + EventTable.TABLE + " (" +
                EventTable.COL_ID + " integer primary key autoincrement, " +
                EventTable.COL_TITLE + " text, " +
                EventTable.COL_DATE + " integer, " +
                EventTable.COL_DESCRIPTION + " text, " +
                EventTable.COL_EVENT_USERNAME + " text, " +
                "foreign key(" + EventTable.COL_EVENT_USERNAME + ") references " +
                UserTable.TABLE + "(" + UserTable.COL_USERNAME + ") on delete cascade)");

        // Adding administrator for testing
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, "admin");
        values.put(UserTable.COL_PASSWORD, "1234");
        values.put(UserTable.COL_SMS_BOOL, "3");
        db.insert(UserTable.TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db = getWritableDatabase();
        db.execSQL("drop table if exists " + UserTable.TABLE);
        db.execSQL("drop table if exists " + EventTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }

    // get list of events
    public User getUser(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + UserTable.TABLE
                + " where " + UserTable.COL_USERNAME + " = '" + username +"'";

        Cursor cursor = db.rawQuery(sql, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user.setUsername(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                user.setSmsBool(cursor.getInt(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return user;
    }

    // add a user given a new user object
    public boolean addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, user.getUsername());
        values.put(UserTable.COL_PASSWORD, user.getPassword());
        values.put(UserTable.COL_SMS_BOOL, 3);
        long id = db.insert(UserTable.TABLE, null, values);
        return id != -1;
    }

    // update a user given the user object
    public void updateUser(User user, String options, String newData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        switch(options) {
            case "username":
                values.put(UserTable.COL_USERNAME, newData);
                break;
            case "password":
                values.put(UserTable.COL_PASSWORD, newData);
                break;
            case "smsBool":
                values.put(UserTable.COL_SMS_BOOL, Integer.parseInt(newData));
            default:
                values.clear();
                break;
        }
        values.put(UserTable.COL_SMS_BOOL, user.getSmsBool());

        db.update(UserTable.TABLE, values,
                UserTable.COL_USERNAME + " = ?", new String[] { user.getUsername() });
    }

    // delete a user given their user object
    public void deleteUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(UserTable.TABLE,
                UserTable.COL_USERNAME + " = ?", new String[] { user.getUsername() });
    }

    public boolean authenticateUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String USERNAME = user.getUsername();
        final String PASSWORD = user.getPassword();

        String sql = "select * from " + UserTable.TABLE
                + " where " + UserTable.COL_USERNAME + " = '" + user.getUsername() +"'";
        Cursor cursor = db.rawQuery(sql, null);
        // Making sure cursor only returns one user, otherwise we have a big problem...
        if (!cursor.moveToFirst()) {
            return false;
        } else if (cursor.getString(0).equals(USERNAME) && cursor.getString(1).equals(PASSWORD)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String USERNAME = user.getUsername();

        if (USERNAME.length() > 0) {
            String sql = "select " + UserTable.COL_USERNAME +" from " + UserTable.TABLE
                    + " where " + UserTable.COL_USERNAME + " = '" + user.getUsername() +"'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                return false;
            } else {
                this.addUser(user);
                return true;
            }
        } else {
            return false;
        }
    }

    // get list of events
    public List<Event> getEvents(String username) {
        List<Event> events = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        final String orderBy = EventTable.COL_DATE + " asc";

        String sql = "select * from " + EventTable.TABLE +
                " where " + EventTable.COL_EVENT_USERNAME + " = '" + username +
                "' order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                Calendar cal = formatDate(cursor.getLong(2));
                event.setDate(cal);
                event.setDescription(cursor.getString(3));
                event.setEventusername(cursor.getString(4));
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return events;
    }

    public Event getEvent(int eId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Event event = new Event();

        String sql = "select * from " + EventTable.TABLE +
                " where " + EventTable.COL_ID + " = " + eId;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                event.setId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                Calendar cal = formatDate(cursor.getLong(2));
                event.setDate(cal);
                event.setDescription(cursor.getString(3));
                event.setEventusername(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return event;
    }

    // add an event given a new event object
    public boolean addEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Calendar eventDate = event.getDate();
        String formatDate = Integer.toString(eventDate.get(Calendar.YEAR)) +
                String.format("%02d", eventDate.get(Calendar.MONTH)) +
                String.format("%02d", eventDate.get(Calendar.DAY_OF_MONTH)) +
                String.format("%02d", eventDate.get(Calendar.HOUR_OF_DAY)) +
                String.format("%02d", eventDate.get(Calendar.MINUTE));

        values.put(EventTable.COL_TITLE, event.getTitle());
        values.put(EventTable.COL_DATE, Long.parseLong(formatDate));
        values.put(EventTable.COL_DESCRIPTION, event.getDescription());
        values.put(EventTable.COL_EVENT_USERNAME, event.getEventusername());
        long id = db.insert(EventTable.TABLE, null, values);
        event.setId(Math.toIntExact(id));
        return id != -1;
    }

    // update an event given the event object
    public boolean updateEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Calendar eventDate = event.getDate();
        String formatDate = Integer.toString(eventDate.get(Calendar.YEAR)) +
                String.format("%02d", eventDate.get(Calendar.MONTH)) +
                String.format("%02d", eventDate.get(Calendar.DAY_OF_MONTH)) +
                String.format("%02d", eventDate.get(Calendar.HOUR_OF_DAY)) +
                String.format("%02d", eventDate.get(Calendar.MINUTE));

        values.put(EventTable.COL_ID, event.getId());
        values.put(EventTable.COL_TITLE, event.getTitle());
        values.put(EventTable.COL_DATE, Long.parseLong(formatDate));
        values.put(EventTable.COL_DESCRIPTION, event.getDescription());
        values.put(EventTable.COL_EVENT_USERNAME, event.getEventusername());

        db.update(EventTable.TABLE, values,
                EventTable.COL_ID + " = ?", new String[] { Integer.toString(event.getId()) });

        return true;
    }

    // delete an event given the event object
    public void deleteEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(EventTable.TABLE,
                EventTable.COL_ID + " = ?", new String[] { Integer.toString(event.getId()) });
    }

    public Calendar formatDate(long longDate) {
        String sDate = Long.toString(longDate);
        int iYear = Integer.parseInt(sDate.substring(0, 4));
        int iMonth = Integer.parseInt(sDate.substring(4, 6)) - 1;
        int iDay = Integer.parseInt(sDate.substring(6, 8));
        int iHour = Integer.parseInt(sDate.substring(8, 10));
        int iMinute = Integer.parseInt(sDate.substring(10, 12));
        Calendar cal = Calendar.getInstance();
        cal.set(iYear, iMonth, iDay, iHour, iMinute);
        return cal;
    }
}
