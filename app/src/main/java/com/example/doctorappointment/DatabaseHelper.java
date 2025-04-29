package com.example.doctorappointment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DoctorAppointment.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_DOCTORS = "doctors";
    private static final String TABLE_APPOINTMENTS = "appointments";

    // Common column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Doctors Table columns
    private static final String COLUMN_SPECIALIZATION = "specialization";
    private static final String COLUMN_AVAILABILITY = "availability";

    // Appointments Table columns
    private static final String COLUMN_DOCTOR_ID = "doctor_id";
    private static final String COLUMN_PATIENT_ID = "patient_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_NOTES = "notes";

    // Create table queries
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_SPECIALIZATION + " TEXT,"
            + COLUMN_AVAILABILITY + " TEXT"
            + ")";

    private static final String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_ID + " INTEGER,"
            + COLUMN_PATIENT_ID + " INTEGER,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_TIME + " TEXT,"
            + COLUMN_STATUS + " TEXT,"
            + COLUMN_NOTES + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DOCTORS_TABLE);
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
        
        // Add some sample doctors
        insertSampleDoctors(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertSampleDoctors(SQLiteDatabase db) {
        String[] doctors = {
            "Dr. John Smith|john.smith@example.com|Cardiology|Mon-Fri 9:00-17:00",
            "Dr. Sarah Johnson|sarah.johnson@example.com|Pediatrics|Mon-Thu 8:00-16:00",
            "Dr. Michael Brown|michael.brown@example.com|Orthopedics|Tue-Sat 10:00-18:00"
        };

        for (String doctor : doctors) {
            String[] parts = doctor.split("\\|");
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, parts[0]);
            values.put(COLUMN_EMAIL, parts[1]);
            values.put(COLUMN_SPECIALIZATION, parts[2]);
            values.put(COLUMN_AVAILABILITY, parts[3]);
            db.insert(TABLE_DOCTORS, null, values);
        }
    }

    // Existing user-related methods
    public long registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return valid;
    }

    // New appointment-related methods
    public long bookAppointment(long doctorId, long patientId, String date, String time, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_ID, doctorId);
        values.put(COLUMN_PATIENT_ID, patientId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_STATUS, "SCHEDULED");
        values.put(COLUMN_NOTES, notes);
        long id = db.insert(TABLE_APPOINTMENTS, null, values);
        db.close();
        return id;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS,
                null, null, null, null, null,
                COLUMN_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_SPECIALIZATION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AVAILABILITY))
                );
                doctors.add(doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return doctors;
    }

    public boolean isTimeSlotAvailable(long doctorId, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPOINTMENTS,
                new String[]{COLUMN_ID},
                COLUMN_DOCTOR_ID + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_TIME + "=? AND " + COLUMN_STATUS + "=?",
                new String[]{String.valueOf(doctorId), date, time, "SCHEDULED"},
                null, null, null);
        boolean isAvailable = cursor.getCount() == 0;
        cursor.close();
        db.close();
        return isAvailable;
    }

    public static class Doctor {
        public final long id;
        public final String name;
        public final String email;
        public final String specialization;
        public final String availability;

        public Doctor(long id, String name, String email, String specialization, String availability) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.specialization = specialization;
            this.availability = availability;
        }

        @Override
        public String toString() {
            return name + " (" + specialization + ")";
        }
    }
} 