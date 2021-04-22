package com.example.aznotes;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ProfileInfoActivityTest {
    @Rule
    public ActivityTestRule<ProfileInfoActivity> detailActivityTestRule = new ActivityTestRule<>(ProfileInfoActivity.class);
    private Context context;
    @Before
    public void init() {
        context = detailActivityTestRule.getActivity().getBaseContext();
    }
    // Test18
    @Test
    public void checkProfileInfoActivityButtonEscribirNota() {
        onView(withText("Escribir nota")).perform(click());
        assertTrue(ProfileInfoActivity.ProfileInfoActivityTest);
    }

    // Test19
    @Test
    public void checkProfileInfoActivityButtonIrAlMenu() {
        onView(withText("Ir al menu")).perform(click());
        assertTrue(ProfileInfoActivity.ProfileInfoActivityTest);
    }

    // Test20
    @Test
    public void checkProfileInfoActivityButtonSalir() {
        onView(withText("Salir")).perform(click());
        assertTrue(ProfileInfoActivity.ProfileInfoActivityTest);
    }




    @After
    public void tearDown() throws Exception {
        // Log.wtf("variable boolean after", LoginActivity.changingScene+"");
        //onView(withId(R.id.saludoProfile)).check(matches(withText("Hola, Kevin Contreras")));
    }

}