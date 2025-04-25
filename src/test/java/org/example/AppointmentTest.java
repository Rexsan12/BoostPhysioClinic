package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class AppointmentTest {
    private Appointment appointment;
    private Patient patient;

    @BeforeEach
    void setUp() {
        Physiotherapist physio = new Physiotherapist("PH001", "Dr. Helen", "1 Street", "1234567890");
        Treatment treatment = new Treatment("Massage Therapy", "Massage");
        appointment = new Appointment(
                "A-M1",
                LocalDateTime.of(2025, 5, 1, 10, 0),
                physio,
                treatment
        );
        patient = new Patient("PT1", "John Doe", "Address 1", "0712345678");
    }

    @Test
    void testBookAppointment() {
        assertTrue(appointment.book(patient));
        assertEquals(AppointmentStatus.BOOKED, appointment.getStatus());
    }

    @Test
    void testCancelAppointment() {
        appointment.book(patient);
        appointment.cancel();
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
    }

    @Test
    void testAttendAppointment() {
        appointment.book(patient);
        appointment.attend();
        assertEquals(AppointmentStatus.ATTENDED, appointment.getStatus());
    }

    @Test
    void testIsAvailable() {
        assertTrue(appointment.isAvailable());
        appointment.book(patient);
        assertFalse(appointment.isAvailable());
    }
}
