package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientTest {
    private BookingSystem system;

    @BeforeEach
    void setUp() {
        system = new BookingSystem();
        system.setupSampleData();
    }

    @Test
    void testAddPatient() {
        assertTrue(system.addPatient("PT13", "John Doe", "Address 13", "0712345678"));
        assertNotNull(system.getPatients().get("PT13"));
    }

    @Test
    void testAddDuplicatePatient() {
        assertFalse(system.addPatient("PT1", "Duplicate", "Addr", "000000"));
    }

    @Test
    void testRemovePatient() {
        assertTrue(system.removePatient("PT1"));
        assertNull(system.getPatients().get("PT1"));
    }

    @Test
    void testRemoveNonExistentPatient() {
        assertFalse(system.removePatient("FAKE"));
    }
}
