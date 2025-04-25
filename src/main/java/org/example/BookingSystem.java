package org.example;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BookingSystem {
    private Map<String, Patient> patients = new HashMap<>();
    private List<Physiotherapist> physiotherapists = new ArrayList<>();
    private Map<String, Appointment> allAppointments = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        setupSampleData();
        int choice;
        do {
            System.out.println("\n*****===== Boost Physio Clinic =====****");
            System.out.println("1. Patient Management");
            System.out.println("2. View Physios & Treatments");
            System.out.println("3. Book Appointment");
            System.out.println("4. Attend Appointment");
            System.out.println("5. Change Appointment Slot");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. View Available Treatments");
            System.out.println("8. Generate Report");
            System.out.println("9. View Available Appointments");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            choice = readInt();

            switch (choice) {
                case 1 -> handlePatientManagement();
                case 2 -> viewPhysiotherapistsAndTreatments();
                case 3 -> bookAppointment();
                case 4 -> attendAppointment();
                case 5 -> changeAppointment();
                case 6 -> cancelAppointment();
                case 7 -> viewAvailableTreatments();
                case 8 -> printReport();
                case 9 -> viewAvailableAppointments();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a number: ");
            scanner.next();
        }
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }

    public void setupSampleData() {
        Physiotherapist p1 = new Physiotherapist("PH001", "Dr. Rex", "1 Street", "1234567890");
        Physiotherapist p2 = new Physiotherapist("PH002", "Dr. John", "2 Avenue", "0987654321");
        Physiotherapist p3 = new Physiotherapist("PH003", "Dr. Lucy", "3 Blvd", "5556667777");

        p1.addExpertise("Massage");
        p2.addExpertise("Rehabilitation");
        p3.addExpertise("Osteopathy");

        Treatment t1 = new Treatment("Massage Therapy", "Massage");
        Treatment t2 = new Treatment("Pool Rehabilitation", "Rehabilitation");
        Treatment t3 = new Treatment("Joint Mobilisation", "Osteopathy");

        for (int i = 0; i < 4; i++) {
            t1.addAppointment(new Appointment("A-M" + (i + 1),
                    LocalDateTime.of(2025, 5, 1 + i * 7, 10, 0), p1, t1));
            t2.addAppointment(new Appointment("A-R" + (i + 1),
                    LocalDateTime.of(2025, 5, 2 + i * 7, 11, 0), p2, t2));
            t3.addAppointment(new Appointment("A-O" + (i + 1),
                    LocalDateTime.of(2025, 5, 3 + i * 7, 9, 0), p3, t3));
        }

        p1.addTreatment(t1);
        p2.addTreatment(t2);
        p3.addTreatment(t3);

        physiotherapists.addAll(Arrays.asList(p1, p2, p3));

        for (int i = 1; i <= 12; i++) {
            String id = "PT" + i;
            patients.put(id, new Patient(id, "Patient " + i, "Address " + i, "07" + i + i + i + "00000"));
        }
    }

    private void handlePatientManagement() {
        System.out.println("\n-- Patient Management --");
        System.out.println("1. Add Patient");
        System.out.println("2. Remove Patient");
        System.out.print("Choice: ");
        int c = readInt();

        if (c == 1) {
            System.out.print("ID: "); String id = scanner.nextLine();
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Address: "); String addr = scanner.nextLine();
            System.out.print("Phone: "); String phone = scanner.nextLine();
            boolean added = patients.putIfAbsent(id, new Patient(id, name, addr, phone)) == null;
            System.out.println(added ? "Added." : "Duplicate ID.");
        } else if (c == 2) {
            System.out.print("ID to remove: ");
            String id = scanner.nextLine();
            System.out.println(patients.remove(id) != null ? "Removed." : "Not found.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewPhysiotherapistsAndTreatments() {
        for (Physiotherapist p : physiotherapists) {
            System.out.println("\n" + p);
            for (Treatment t : p.getTreatments()) {
                System.out.println("  ▶ " + t);
                for (Appointment a : t.getAppointments()) {
                    System.out.println("      " + a);
                    allAppointments.put(a.getAppointmentId(), a);
                }
            }
        }
    }

    private void bookAppointment() {
        System.out.println("\n1) By Expertise   2) By Physiotherapist");
        System.out.print("Choice: ");
        int m = readInt();
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine();

        boolean ok = false;
        if (m == 1) {
            System.out.print("Expertise: ");
            ok = bookByExpertise(scanner.nextLine(), pid);
        } else if (m == 2) {
            System.out.print("Physio Name: ");
            ok = bookByPhysiotherapist(scanner.nextLine(), pid);
        } else {
            System.out.println("Invalid method.");
        }
        System.out.println(ok ? "Booked." : "Failed.");
    }

    public boolean bookByExpertise(String exp, String pid) {
        Patient pat = patients.get(pid);
        if (pat == null) return false;
        List<Appointment> avail = physiotherapists.stream()
                .filter(p -> p.getAreasOfExpertise().contains(exp))
                .flatMap(p -> p.getTreatments().stream())
                .flatMap(t -> t.getAppointments().stream())
                .filter(Appointment::isAvailable)
                .collect(Collectors.toList());
        if (avail.isEmpty()) return false;
        Appointment a = avail.get(0);
        a.book(pat);
        allAppointments.put(a.getAppointmentId(), a);
        return true;
    }

    public boolean bookByPhysiotherapist(String name, String pid) {
        Patient pat = patients.get(pid);
        if (pat == null) return false;
        for (Physiotherapist p : physiotherapists) {
            if (p.getFullName().equalsIgnoreCase(name)) {
                for (Treatment t : p.getTreatments()) {
                    for (Appointment a : t.getAppointments()) {
                        if (a.isAvailable()) {
                            a.book(pat);
                            allAppointments.put(a.getAppointmentId(), a);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void attendAppointment() {
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine();
        List<Appointment> list = allAppointments.values().stream()
                .filter(a -> a.getPatient() != null
                        && a.getPatient().getId().equals(pid)
                        && a.getStatus() == AppointmentStatus.BOOKED)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            System.out.println("None to attend.");
            return;
        }
        list.forEach(a -> System.out.println(a.getAppointmentId() + ": " + a));
        System.out.print("Appt ID to mark attended: ");
        Appointment a = allAppointments.get(scanner.nextLine());
        if (a != null && a.getStatus() == AppointmentStatus.BOOKED) {
            a.attend();
            System.out.println("Marked attended.");
        } else {
            System.out.println("Failed.");
        }
    }

    private void changeAppointment() {
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine();
        Patient p = patients.get(pid);
        if (p == null) {
            System.out.println("No such patient.");
            return;
        }
        List<Appointment> booked = allAppointments.values().stream()
                .filter(a -> a.getPatient() != null
                        && a.getPatient().getId().equals(pid)
                        && a.getStatus() == AppointmentStatus.BOOKED)
                .collect(Collectors.toList());
        if (booked.isEmpty()) {
            System.out.println("No bookings.");
            return;
        }
        for (int i = 0; i < booked.size(); i++) {
            System.out.println((i + 1) + ". " + booked.get(i));
        }
        System.out.print("Select #: ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= booked.size()) {
            System.out.println("Invalid.");
            return;
        }
        Appointment old = booked.get(idx);
        List<Appointment> avail = old.getTreatment().getAppointments().stream()
                .filter(Appointment::isAvailable).collect(Collectors.toList());
        if (avail.isEmpty()) {
            System.out.println("No slots.");
            return;
        }
        for (int i = 0; i < avail.size(); i++) {
            System.out.println((i + 1) + ". " + avail.get(i));
        }
        System.out.print("New slot #: ");
        int ni = readInt() - 1;
        if (ni < 0 || ni >= avail.size()) {
            System.out.println("Invalid.");
            return;
        }
        old.cancel();
        avail.get(ni).book(p);
        allAppointments.put(avail.get(ni).getAppointmentId(), avail.get(ni));
        System.out.println("Changed.");
    }

    private void cancelAppointment() {
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine();
        List<Appointment> list = allAppointments.values().stream()
                .filter(a -> a.getPatient() != null
                        && a.getPatient().getId().equals(pid)
                        && a.getStatus() == AppointmentStatus.BOOKED)
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            System.out.println("None to cancel.");
            return;
        }
        list.forEach(a -> System.out.println(a.getAppointmentId() + ": " + a));
        System.out.print("Appt ID to cancel: ");
        Appointment a = allAppointments.get(scanner.nextLine());
        if (a != null && a.getStatus() == AppointmentStatus.BOOKED) {
            a.cancel();
            System.out.println("Cancelled.");
        } else {
            System.out.println("Failed.");
        }
    }

    private void viewAvailableTreatments() {
        System.out.println("Available Treatments:");
        physiotherapists.forEach(p ->
                p.getTreatments().forEach(t ->
                        System.out.println(" - " + t)));
    }

    private void viewAvailableAppointments() {
        System.out.println("Available Appointments:");
        allAppointments.values().stream()
                .filter(Appointment::isAvailable)
                .forEach(a ->
                        System.out.println(" - " + a.getAppointmentId() + ": " + a.getDateTime()
                                + " [" + a.getTreatment().getName() + "]"));
    }

    public void printReport() {
        System.out.println("\n=== Report: All Appointments ===");
        for (Physiotherapist p : physiotherapists) {
            System.out.println("\n" + p.getFullName());
            for (Treatment t : p.getTreatments()) {
                for (Appointment a : t.getAppointments()) {
                    System.out.println("  " + a);
                }
            }
        }
        System.out.println("\n=== Ranking by Attended ===");
        physiotherapists.stream()
                .sorted((a, b) -> Long.compare(countAttended(b), countAttended(a)))
                .forEach(p ->
                        System.out.println(p.getFullName()
                                + " - Attended: " + countAttended(p)));
    }

    private long countAttended(Physiotherapist p) {
        return p.getTreatments().stream()
                .flatMap(t -> t.getAppointments().stream())
                .filter(a -> a.getStatus() == AppointmentStatus.ATTENDED)
                .count();
    }

    // ===== Public methods for testing =====

    public Map<String, Patient> getPatients() {
        return patients;
    }

    public boolean addPatient(String id, String name, String address, String phone) {
        return patients.putIfAbsent(id, new Patient(id, name, address, phone)) == null;
    }

    public boolean removePatient(String id) {
        return patients.remove(id) != null;
    }

    public boolean cancelAppointmentByPatientId(String patientId) {
        return allAppointments.values().stream()
                .filter(a -> a.getPatient() != null && a.getPatient().getId().equals(patientId) && a.getStatus() == AppointmentStatus.BOOKED)
                .findFirst()
                .map(a -> {
                    a.cancel();
                    return true;
                })
                .orElse(false);
    }

    public boolean attendAppointmentByPatientId(String patientId) {
        return allAppointments.values().stream()
                .filter(a -> a.getPatient() != null && a.getPatient().getId().equals(patientId) && a.getStatus() == AppointmentStatus.BOOKED)
                .findFirst()
                .map(a -> {
                    a.attend();
                    return true;
                })
                .orElse(false);
    }

    public boolean changeAppointmentForPatient(String patientId) {
        Patient p = patients.get(patientId);
        if (p == null) return false;

        List<Appointment> booked = allAppointments.values().stream()
                .filter(a -> a.getPatient() != null && a.getPatient().getId().equals(patientId) && a.getStatus() == AppointmentStatus.BOOKED)
                .collect(Collectors.toList());

        if (booked.isEmpty()) return false;

        Appointment old = booked.get(0);
        List<Appointment> avail = old.getTreatment().getAppointments().stream()
                .filter(Appointment::isAvailable)
                .collect(Collectors.toList());

        if (avail.isEmpty()) return false;

        Appointment newSlot = avail.get(0);
        old.cancel();
        newSlot.book(p);
        allAppointments.put(newSlot.getAppointmentId(), newSlot);
        return true;
    }
}
