package com.journear.app;
import com.journear.app.core.entities.NearbyDevice;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreferencesUnitTest {
    @Test
    public void test_gendercheck1() {
        assertFalse(NearbyDevice.checkGender("M", true, "F", false));
    }
    @Test
    public void test_gendercheck2() {
        assertTrue(NearbyDevice.checkGender("M", true, "M", false));
    }
    @Test
    public void test_gendercheck3() {
        assertFalse(NearbyDevice.checkGender("M", true, "F", true));
    }
    @Test
    public void test_gendercheck4() {
        assertFalse(NearbyDevice.checkGender("M", true, "F", false));
    }
    @Test
    public void test_gendercheck5() {
        assertFalse(NearbyDevice.checkGender("M", true, "F", true));
    }
    @Test
    public void test_gendercheck6() {
        assertTrue(NearbyDevice.checkGender("F", true, "F", false));
    }
    @Test
    public void test_gendercheck7() {
        assertFalse(NearbyDevice.checkGender("F", true, "A", false));
    }
    @Test
    public void test_gendercheck8() {
        assertTrue(NearbyDevice.checkGender("A", true, "A", true));
    }
    @Test
    public void test_gendercheck9() {
        assertTrue(NearbyDevice.checkGender("A", false, "A", false));
    }
    @Test
    public void test_gendercheck10() {
        assertTrue(NearbyDevice.checkGender("F", false, "F", false));
    }
}
