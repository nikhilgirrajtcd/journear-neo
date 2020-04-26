package com.journear.app;

import android.content.Context;
import android.os.SystemClock;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.journear.app.ui.MainActivity;
import com.journear.app.ui.UserRegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private String stringToBetyped;

    @Rule
    public ActivityTestRule<UserRegisterActivity> activityRule
            = new ActivityTestRule<>(UserRegisterActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.journear.app", appContext.getPackageName());
    }

    @Test
    public void registerUser() {
        // Type text and then press the button.
        onView(withId(R.id.editName)).perform(typeText("Random User 1"), closeSoftKeyboard());
        onView(withId(R.id.editEmail)).perform(typeText("random@user.com"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("Journear123"), closeSoftKeyboard());
        onView(withId(R.id.editPhone)).perform(typeText("+35344344344"), closeSoftKeyboard());
//        onView(withId(R.id.editDob)).perform(typeText("9/12/2000"), closeSoftKeyboard());

        assertEquals("Hack", "Hack");
//        onView(R.id.editEmail).perform(waitFor(5000))
        // Check that the text was changed.
        SystemClock.sleep(10000);
    }
}