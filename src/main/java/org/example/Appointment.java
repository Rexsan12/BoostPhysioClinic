package org.example;

import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private LocalDateTime dateTime;
    private Physiotherapist physiotherapist;
    private Treatment treatment;
    private Patient patient; // null until booked
    private AppointmentStatus status;

    public Appointment(String appointmentId, LocalDateTime dateTime,
                       Physiotherapist physiotherapist, Treatment treatment) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
        this.treatment = treatment;
        this.status = AppointmentStatus.AVAILABLE;
    }

    public String getAppointmentId()   { return appointmentId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Physiotherapist getPhysiotherapist() { return physiotherapist; }
    public Treatment getTreatment()    { return treatment; }
    public Patient getPatient()        { return patient; }
    public AppointmentStatus getStatus() { return status; }

    public boolean book(Patient patient) {
        if (status == AppointmentStatus.AVAILABLE) {
            this.patient = patient;
            this.status = AppointmentStatus.BOOKED;
            return true;
        }
        return false;
    }

    public void cancel() {
        if (status == AppointmentStatus.BOOKED) {
            status = AppointmentStatus.CANCELLED;
            patient = null;
        }
    }

    public void attend() {
        if (status == AppointmentStatus.BOOKED) {
            status = AppointmentStatus.ATTENDED;
        }
    }

    public boolean isAvailable() {
        return status == AppointmentStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "[" + dateTime + "] " + treatment.getName()
                + " with " + physiotherapist.getFullName()
                + " - Status: " + status
                + (patient != null ? " (Patient: " + patient.getFullName() + ")" : "");
    }
}
