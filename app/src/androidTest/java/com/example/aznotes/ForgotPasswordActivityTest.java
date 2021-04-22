package com.example.aznotes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ForgotPasswordActivityTest {
    @Rule
    public ActivityTestRule<ForgotPassword> detailActivityTestRule = new ActivityTestRule<>(ForgotPassword.class);
    private Context context;
    @Before
    public void init() {
        context = detailActivityTestRule.getActivity().getBaseContext();
    }
    // Test14
    @Test
    public void checkForgotPasswordWithEmptyEmail() {
        onView(withId(R.id.emailEditText )).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withText("RECUPERAR")).perform(click());
        assertTrue(!ForgotPassword.forgotPasswordFlag);
    }

    // Test15
    @Test
    public void checkForgotPasswordWithCorrectEmail() {
        onView(withId(R.id.emailEditText )).perform(typeText("a01636172@gmail.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("RECUPERAR")).perform(click());
        assertTrue(ForgotPassword.forgotPasswordFlag);
    }


    // Test16
    @Test
    public void checkForgotPasswordWithInvalidEmail() {
        onView(withId(R.id.emailEditText )).perform(typeText("correo_invalido"));
        Espresso.closeSoftKeyboard();
        onView(withText("RECUPERAR")).perform(click());
        assertTrue(!ForgotPassword.forgotPasswordFlag);
    }

    // Test17
    @Test
    public void checkForgotPasswordWithNoEmailRegistered() {
        onView(withId(R.id.emailEditText )).perform(typeText("correo@gmail.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("RECUPERAR")).perform(click());
        assertTrue(ForgotPassword.forgotPasswordFlag);
    }

    @After
    public void tearDown() throws Exception {
        // Log.wtf("variable boolean after", LoginActivity.changingScene+"");
        //onView(withId(R.id.saludoProfile)).check(matches(withText("Hola, Kevin Contreras")));
    }

}