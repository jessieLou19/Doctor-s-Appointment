package com.example.doctorappointment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DoctorAppointment.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_DOCTORS = "doctors";
    public static final String TABLE_APPOINTMENTS = "appointments";

    // Common column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";

    // Users Table columns
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_USER_TYPE = "user_type";

    // Doctors Table columns
    public static final String COLUMN_SPECIALIZATION = "specialization";
    public static final String COLUMN_AVAILABILITY = "availability";

    // Appointments Table columns
    public static final String COLUMN_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_PATIENT_ID = "patient_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "status";

    // Create table statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_PHONE + " TEXT,"
            + COLUMN_USER_TYPE + " TEXT"
            + ")";

    private static final String CREATE_TABLE_DOCTORS = "CREATE TABLE " + TABLE_DOCTORS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_SPECIALIZATION + " TEXT,"
            + COLUMN_AVAILABILITY + " TEXT"
            + ")";

    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE " + TABLE_APPOINTMENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_ID + " INTEGER,"
            + COLUMN_PATIENT_ID + " INTEGER,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_TIME + " TEXT,"
            + COLUMN_STATUS + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DOCTORS);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }
} 