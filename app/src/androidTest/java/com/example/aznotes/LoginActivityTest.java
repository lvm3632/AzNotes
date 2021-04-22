package com.example.aznotes;

import android.content.Context;
import android.util.Log;

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
public class LoginActivityTest {



    @Rule
    public ActivityTestRule<LoginActivity> detailActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    //@Rule
    //public ActivityTestRule<ProfileInfoActivityTest> profileTestRule = new ActivityTestRule<>(ProfileInfoActivityTest.class);


    private Context context, context2;

    @Before
    public void init() {
        context = detailActivityActivityTestRule.getActivity().getBaseContext();
       // context2 = profileTestRule.getActivity().getBaseContext();

        //profileTestRule.getActivity().onStart();
        //Log.wtf("variable boolean init", LoginActivity.changingScene+"");
    }
    // Test1
    @Test
    public void checkLoginWithValidCredentials() {
        onView(withId(R.id.emailEditText)).perform(typeText("kevin@test.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("test12"));
        Espresso.closeSoftKeyboard();
        onView(withText("INICIO")).perform(click());
        assertTrue(LoginActivity.changingScene);
    }

    //Test2
    @Test
    public void checkLoginWithInvalidEmailAndPasswordCredentials() {
        onView(withId(R.id.emailEditText)).perform(typeText("correo_invalido"));
        onView(withId(R.id.passwordEditText)).perform(typeText("aaaaaa123"));
        Espresso.closeSoftKeyboard();
        onView(withText("INICIO")).perform(click());
        Log.wtf("variable boolean invalid", LoginActivity.changingScene+"");
        assertTrue(!LoginActivity.changingScene);
    }

    //Test3
    @Test
    public void checkLoginWithEmptyCredentials() {
        onView(withId(R.id.emailEditText)).perform(typeText(""));
        onView(withId(R.id.passwordEditText)).perform(typeText(""));
        Espresso.closeSoftKeyboard();
        onView(withText("INICIO")).perform(click());
        Log.wtf("variable boolean invalid", LoginActivity.changingScene+"");
        assertTrue(!LoginActivity.changingScene);
    }

    // Test4
    @Test
    public void checkLoginWithCorrectEmailAndWrongPasswordCredentials() {
        Log.wtf("EmailAndWrongPasswordboolean", LoginActivity.changingScene+"");
        onView(withId(R.id.emailEditText)).perform(typeText("kevin@test.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("aaaaaa123"));
        Espresso.closeSoftKeyboard();
        onView(withText("INICIO")).perform(click());
        Log.wtf("EmailAndWrongPasswordboolean", LoginActivity.changingScene+"");
        assertTrue(LoginActivity.changingScene);
    }



    @After
    public void tearDown() throws Exception {
       // Log.wtf("variable boolean after", LoginActivity.changingScene+"");
        //onView(withId(R.id.saludoProfile)).check(matches(withText("Hola, Kevin Contreras")));
    }





}