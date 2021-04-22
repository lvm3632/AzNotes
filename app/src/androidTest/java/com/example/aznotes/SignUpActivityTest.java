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
public class SignUpActivityTest {



    @Rule
    public ActivityTestRule<SignUpActivity> detailActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);


    private Context context;

    @Before
    public void init() {
        context = detailActivityTestRule.getActivity().getBaseContext();
    }
    // Test5
    @Test
    public void checkSignUpWithEmptyName() {
        onView(withId(R.id.nombreTextField)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("unonuevo@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test6
    @Test
    public void checkSignUpWithEmptyMail() {
        onView(withId(R.id.nombreTextField)).perform(typeText("kevin"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test7
    @Test
    public void checkSignUpWithEmptyPassword() {
        onView(withId(R.id.nombreTextField)).perform(typeText("michel"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("123456"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("unonuevo@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test8
    @Test
    public void checkSignUpWithEmptyPasswordAndConfirmPasswordEmpty() {
        onView(withId(R.id.nombreTextField)).perform(typeText("michel"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("unonuevo@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test9
    @Test
    public void checkSignUpWithConfirmPasswordEmpty() {
        onView(withId(R.id.nombreTextField)).perform(typeText("michel"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("unonuevo@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test10
    @Test
    public void checkSignUpWithIncorrectEmail() {
        onView(withId(R.id.nombreTextField)).perform(typeText("michel"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("email_incorrecto"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test11
    @Test
    public void checkSignUpWithEmailPreviouslyRegistered() {
        onView(withId(R.id.nombreTextField)).perform(typeText("michel"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("kevin@test.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(SignUpActivity.signUpFlag);
    }

    // Test12
    @Test
    public void checkSignUpWithNotRepeatedPasswords() {
        onView(withId(R.id.nombreTextField)).perform(typeText("michel"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("hola123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("comoestas123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText("nuevocorreo@gmail.com"));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    // Test13
    @Test
    public void checkSignUpWithEmptyFields() {
        onView(withId(R.id.nombreTextField)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withText("REGISTRAR")).perform(click());
        assertTrue(!SignUpActivity.signUpFlag);
    }

    @After
    public void tearDown() throws Exception {
        // Log.wtf("variable boolean after", LoginActivity.changingScene+"");
        //onView(withId(R.id.saludoProfile)).check(matches(withText("Hola, Kevin Contreras")));
    }

}