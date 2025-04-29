package com.example.doctorappointment.models;

import java.util.Date;

public class Appointment {
    private long id;
    private long doctorId;
    private long patientId;
    private Date date;
    private String time;
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String notes;

    public Appointment(long id, long doctorId, long patientId, Date date, String time, String status, String notes) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.notes = notes;
    }

    // Getters
    public long getId() { return id; }
    public long getDoctorId() { return doctorId; }
    public long getPatientId() { return patientId; }
    public Date getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setDoctorId(long doctorId) { this.doctorId = doctorId; }
    public void setPatientId(long patientId) { this.patientId = patientId; }
    public void setDate(Date date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
} 