package com.example.doctorappointment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.doctorappointment.models.Appointment;
import com.example.doctorappointment.models.Doctor;
import com.example.doctorappointment.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eclinic.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_DOCTORS = "doctors";
    private static final String TABLE_APPOINTMENTS = "appointments";
    private static final String TABLE_NOTIFICATIONS = "notifications";
    
    // Common column names
    private static final String KEY_ID = "id";
    
    // Users table columns
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ROLE = "role";
    
    // Doctors table columns
    private static final String KEY_SPECIALIZATION = "specialization";
    private static final String KEY_AVAILABILITY = "availability";
    
    // Appointments table columns
    private static final String KEY_PATIENT_ID = "patient_id";
    private static final String KEY_DOCTOR_ID = "doctor_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_STATUS = "status";
    private static final String KEY_NOTES = "notes";
    
    // Notifications table columns
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_READ = "read";
    private static final String KEY_CREATED_AT = "created_at";
    
    // Date format for database
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_ROLE + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        
        // Create doctors table
        String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_SPECIALIZATION + " TEXT,"
                + KEY_AVAILABILITY + " TEXT"
                + ")";
        db.execSQL(CREATE_DOCTORS_TABLE);
        
        // Create appointments table
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PATIENT_ID + " INTEGER,"
                + KEY_DOCTOR_ID + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + "FOREIGN KEY(" + KEY_PATIENT_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTORS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
        
        // Create notifications table
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_READ + " INTEGER DEFAULT 0,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
        
        // Insert sample data
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Create tables again
        onCreate(db);
    }
    
    private void insertSampleData(SQLiteDatabase db) {
        // Insert sample doctors
        ContentValues values = new ContentValues();
        
        values.put(KEY_NAME, "Dr. Maria Santos");
        values.put(KEY_EMAIL, "maria.santos@buksu.edu.ph");
        values.put(KEY_SPECIALIZATION, "Cardiology");
        values.put(KEY_AVAILABILITY, "Monday, Wednesday, Friday");
        db.insert(TABLE_DOCTORS, null, values);
        
        values.clear();
        values.put(KEY_NAME, "Dr. Juan Cruz");
        values.put(KEY_EMAIL, "juan.cruz@buksu.edu.ph");
        values.put(KEY_SPECIALIZATION, "Pediatrics");
        values.put(KEY_AVAILABILITY, "Tuesday, Thursday");
        db.insert(TABLE_DOCTORS, null, values);
        
        values.clear();
        values.put(KEY_NAME, "Dr. Ana Reyes");
        values.put(KEY_EMAIL, "ana.reyes@buksu.edu.ph");
        values.put(KEY_SPECIALIZATION, "Dermatology");
        values.put(KEY_AVAILABILITY, "Monday, Friday");
        db.insert(TABLE_DOCTORS, null, values);
    }
    
    // User methods
    public long registerUser(String name, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_PHONE, phone);
        values.put(KEY_ROLE, "patient");
        
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        
        return id;
    }
    
    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {KEY_ID, KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_ROLE};
        String selection = KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setRole(cursor.getString(4));
            cursor.close();
        }
        
        db.close();
        return user;
    }
    
    public String getUserName(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {KEY_NAME};
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        String name = null;
        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(0);
            cursor.close();
        }
        
        db.close();
        return name;
    }

    public User getUserById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {KEY_ID, KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_ROLE};
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhone(cursor.getString(3));
            user.setRole(cursor.getString(4));
            cursor.close();
        }
        
        db.close();
        return user;
    }
    
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {KEY_ID};
        String selection = KEY_EMAIL + " = ?";
        String[] selectionArgs = {email};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        int count = cursor.getCount();
        cursor.close();
        db.close();
        
        return count > 0;
    }
    
    // Doctor methods
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_DOCTORS;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor();
                doctor.setId(cursor.getLong(0));
                doctor.setName(cursor.getString(1));
                doctor.setEmail(cursor.getString(2));
                doctor.setSpecialization(cursor.getString(3));
                doctor.setAvailability(cursor.getString(4));
                
                doctors.add(doctor);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return doctors;
    }
    
    // Appointment methods
    public long bookAppointment(long patientId, long doctorId, String date, String time, String status, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_PATIENT_ID, patientId);
        values.put(KEY_DOCTOR_ID, doctorId);
        values.put(KEY_DATE, date);
        values.put(KEY_TIME, time);
        values.put(KEY_STATUS, status);
        values.put(KEY_NOTES, notes);
        
        long id = db.insert(TABLE_APPOINTMENTS, null, values);
        
        if (id != -1) {
            // Create notification for the patient
            createNotification(patientId, "Your appointment has been scheduled for " + date + " at " + time);
        }
        
        db.close();
        
        return id;
    }
    
    public List<Appointment> getPatientAppointments(long patientId) {
        List<Appointment> appointments = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENTS + 
                " WHERE " + KEY_PATIENT_ID + " = ? ORDER BY " + KEY_DATE + ", " + KEY_TIME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(patientId)});
        
        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setId(cursor.getLong(0));
                appointment.setPatientId(cursor.getLong(1));
                appointment.setDoctorId(cursor.getLong(2));
                
                // Parse date
                try {
                    Date date = DATE_FORMAT.parse(cursor.getString(3));
                    appointment.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                
                appointment.setTime(cursor.getString(4));
                appointment.setStatus(cursor.getString(5));
                appointment.setNotes(cursor.getString(6));
                
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return appointments;
    }
    
    public boolean updateAppointmentStatus(long appointmentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);
        
        int rowsAffected = db.update(TABLE_APPOINTMENTS, values, KEY_ID + " = ?", 
                new String[]{String.valueOf(appointmentId)});
        
        if (rowsAffected > 0) {
            // Get patient ID for notification
            String query = "SELECT " + KEY_PATIENT_ID + ", " + KEY_DATE + ", " + KEY_TIME + 
                    " FROM " + TABLE_APPOINTMENTS + " WHERE " + KEY_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(appointmentId)});
            
            if (cursor != null && cursor.moveToFirst()) {
                long patientId = cursor.getLong(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                
                // Create notification
                String message = "Your appointment on " + date + " at " + time + " has been " + status.toLowerCase();
                createNotification(patientId, message);
                
                cursor.close();
            }
        }
        
        db.close();
        
        return rowsAffected > 0;
    }
    
    // Notification methods
    private long createNotification(long userId, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_MESSAGE, message);
        
        long id = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
        
        return id;
    }
    
    public int getUnreadNotificationCount(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_NOTIFICATIONS + 
                " WHERE " + KEY_USER_ID + " = ? AND " + KEY_READ + " = 0";
        
        Cursor cursor = db.rawQuery(countQuery, new String[]{String.valueOf(userId)});
        
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        
        db.close();
        
        return count;
    }
}
