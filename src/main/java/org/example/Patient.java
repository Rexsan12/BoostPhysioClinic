package org.example;

public class Patient extends Person {
    public Patient(String id, String fullName, String address, String phoneNumber) {
        super(id, fullName, address, phoneNumber);
    }

    @Override
    public String toString() {
        return "Patient: " + fullName + " (ID: " + id + ")";
    }
}

