package com.example.login4;


import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityScenarioRule<SignUpActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SignUpActivity.class);

    @Test
    public void testEmailFieldDisplayed() {
        // Check if the email field is displayed
        onView(withId(R.id.signup_email)).check(matches(isDisplayed()));
    }

    @Test
    public void testPasswordFieldDisplayed() {
        // Check if the password field is displayed
        onView(withId(R.id.signup_password)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpButtonDisplayed() {
        // Check if the sign-up button is displayed
        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testPasswordStrengthFeedback() {
        // Input a weak password and check feedback
        onView(withId(R.id.signup_password)).perform(typeText("12345"), closeSoftKeyboard());
        onView(withId(R.id.password_strength_feedback)).check(matches(withText("Weak Password")));

        // Input a medium password and check feedback
        onView(withId(R.id.signup_password)).perform(clearText(), typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.password_strength_feedback)).check(matches(withText("Medium Password")));

        // Input a strong password and check feedback
        onView(withId(R.id.signup_password)).perform(clearText(), typeText("123456789@"), closeSoftKeyboard());
        onView(withId(R.id.password_strength_feedback)).check(matches(withText("Strong Password")));
    }

    @Test
    public void testEmailValidation() {
        // Leave the email field empty and try signing up
        onView(withId(R.id.signup_email)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.signup_button)).perform(click());
        onView(withId(R.id.signup_email)).check(matches(hasErrorText("Email cannot be empty")));
    }

    @Test
    public void testPasswordValidation() {
        // Leave the password field empty and try signing up
        onView(withId(R.id.signup_email)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.signup_password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.signup_button)).perform(click());
        onView(withId(R.id.signup_password)).check(matches(hasErrorText("Password cannot be empty")));
    }

    @Test
    public void testRedirectToLogin() {
        // Click on the login redirect text and check if LoginActivity is launched
        onView(withId(R.id.loginRedirectText)).perform(click());
        onView(withId(R.id.login_email)) // Assuming login_email is the ID in LoginActivity
                .check(matches(isDisplayed()));
    }
}

