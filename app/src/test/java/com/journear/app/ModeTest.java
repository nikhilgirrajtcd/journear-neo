package com.journear.app;
import com.journear.app.MockData_Mode;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class ModeTest {
    @Test
    public void checkModeTest() {
        assertTrue(MockData_Mode.checkMode(MockData_Mode.Mode.ANY, MockData_Mode.Mode.TAXI));
        assertTrue(MockData_Mode.checkMode(MockData_Mode.Mode.WALK, MockData_Mode.Mode.ANY));
        assertTrue(MockData_Mode.checkMode(MockData_Mode.Mode.ANY, MockData_Mode.Mode.ANY));
        assertFalse(MockData_Mode.checkMode(MockData_Mode.Mode.WALK, MockData_Mode.Mode.TAXI));
    }

}
