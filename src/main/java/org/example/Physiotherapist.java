package org.example;

import java.util.ArrayList;
import java.util.List;

public class Physiotherapist extends Person {
    private List<String> areasOfExpertise = new ArrayList<>();
    private List<Treatment> treatments       = new ArrayList<>();

    public Physiotherapist(String id, String fullName, String address, String phoneNumber) {
        super(id, fullName, address, phoneNumber);
    }

    public void addExpertise(String expertise) {
        if (!areasOfExpertise.contains(expertise)) {
            areasOfExpertise.add(expertise);
        }
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    @Override
    public String toString() {
        return "Physiotherapist: " + fullName + " (ID: " + id + ")";
    }
}
