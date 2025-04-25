package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BookingSystemTest {
    private BookingSystem system;

    @BeforeEach
    void setUp() {
        system = new BookingSystem();
        system.setupSampleData();
    }

    @Test
    void testAddAndRemovePatient() {
        Map<String, Patient> pts = system.getPatients();
        int before = pts.size();
        assertTrue(system.addPatient("PT100", "Test", "Addr", "07123456789"));
        assertEquals(before + 1, pts.size());
        assertTrue(system.removePatient("PT100"));
        assertEquals(before, pts.size());
    }

    @Test
    void testBookByExpertise_SuccessAndFail() {
        assertTrue(system.bookByExpertise("Massage", "PT1"));
        assertFalse(system.bookByExpertise("NoSuch", "PT1"));
        assertFalse(system.bookByExpertise("Massage", "BAD"));
    }

    @Test
    void testBookByPhysiotherapist_SuccessAndFail() {
        assertTrue(system.bookByPhysiotherapist("Dr. Rex", "PT2"));
        assertFalse(system.bookByPhysiotherapist("Unknown", "PT2"));
        assertFalse(system.bookByPhysiotherapist("Dr. Rex", "BAD"));
    }

    @Test
    void testCancelAppointmentByPatient() {
        assertFalse(system.cancelAppointmentByPatientId("PT3"));
        system.bookByExpertise("Massage", "PT3");
        assertTrue(system.cancelAppointmentByPatientId("PT3"));
        assertFalse(system.cancelAppointmentByPatientId("PT3"));
    }

    @Test
    void testAttendAppointmentByPatient() {
        assertFalse(system.attendAppointmentByPatientId("PT4"));
        system.bookByExpertise("Massage", "PT4");
        assertTrue(system.attendAppointmentByPatientId("PT4"));
        assertFalse(system.attendAppointmentByPatientId("PT4"));
    }

    @Test
    void testChangeAppointmentForPatient() {
        assertFalse(system.changeAppointmentForPatient("PT5"));
        assertTrue(system.bookByExpertise("Massage", "PT5"));
        assertTrue(system.changeAppointmentForPatient("PT5"));
    }


    @Test
    void testPrintReportDoesNotThrow() {
        assertDoesNotThrow(system::printReport);
    }
}
