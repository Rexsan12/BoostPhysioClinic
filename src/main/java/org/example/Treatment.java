package org.example;

import java.util.ArrayList;
import java.util.List;

public class Treatment {
    private String name;
    private String expertiseArea;
    private List<Appointment> appointments = new ArrayList<>();

    public Treatment(String name, String expertiseArea) {
        this.name = name;
        this.expertiseArea = expertiseArea;
    }

    public String getName() {
        return name;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public String toString() {
        return name + " [" + expertiseArea + "]";
    }
}
