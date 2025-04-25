package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhysiotherapistTest {
    private Physiotherapist physiotherapist;

    @BeforeEach
    void setUp() {
        physiotherapist = new Physiotherapist("PH001", "Dr. Helen", "1 Street", "1234567890");
    }

    @Test
    void testInitialStateEmpty() {
        assertTrue(physiotherapist.getAreasOfExpertise().isEmpty());
        assertTrue(physiotherapist.getTreatments().isEmpty());
    }

    @Test
    void testAddExpertise() {
        physiotherapist.addExpertise("Massage");
        assertTrue(physiotherapist.getAreasOfExpertise().contains("Massage"));
    }

    @Test
    void testAddDuplicateExpertise() {
        physiotherapist.addExpertise("Massage");
        physiotherapist.addExpertise("Massage");
        assertEquals(1, physiotherapist.getAreasOfExpertise().size());
    }

    @Test
    void testAddTreatment() {
        Treatment t = new Treatment("Massage Therapy", "Massage");
        physiotherapist.addTreatment(t);
        assertTrue(physiotherapist.getTreatments().contains(t));
    }
}
