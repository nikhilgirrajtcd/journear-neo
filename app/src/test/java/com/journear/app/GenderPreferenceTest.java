package com.journear.app;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GenderPreferenceTest {
    @Test
    public void checkPreferenceTest() {
        MockData_Gender current_pref = new MockData_Gender(MockData_Gender.Gender.MALE, MockData_Gender.Gender.MALE);
        MockData_Gender search1_pref = new MockData_Gender(MockData_Gender.Gender.MALE, MockData_Gender.Gender.FEMALE);
        MockData_Gender search2_pref = new MockData_Gender(MockData_Gender.Gender.FEMALE, MockData_Gender.Gender.MALE);
        MockData_Gender search3_pref = new MockData_Gender(MockData_Gender.Gender.MALE, MockData_Gender.Gender.MALE);

        assertFalse(current_pref.checkPreferences(search1_pref));
        assertFalse(current_pref.checkPreferences(search2_pref));
        assertTrue(current_pref.checkPreferences(search3_pref));
    }
}
