package com.example.login4;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.action.ViewActions.click;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private FirebaseAuth mAuth;

    @Before
    public void setup() {
        // Initialize FirebaseAuth or any dependencies
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testLoginActivityUI() {
        // Launch LoginActivity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.login4", "com.example.login4.LoginActivity");
        Espresso.onView(ViewMatchers.withId(R.id.login_email)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.login_password)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.login_button)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.signUpRedirectText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.forgot_password)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithEmptyEmail() {
        // Click login button with empty email field
        Espresso.onView(withId(R.id.login_button)).perform(click());

        // Check if error is shown for empty email field
        Espresso.onView(withId(R.id.login_email))
                .check(matches(hasErrorText("Email cannot be empty")));
    }

    @Test
    public void testLoginWithInvalidEmail() {
        // Enter an invalid email and try to log in
        Espresso.onView(withId(R.id.login_email)).perform(ViewActions.typeText("invalidemail"));
        Espresso.onView(withId(R.id.login_button)).perform(click());

        // Check if the error message for invalid email is shown
        Espresso.onView(withId(R.id.login_email))
                .check(matches(hasErrorText("Enter a valid email address")));
    }

    @Test
    public void testLoginWithEmptyPassword() {
        // Enter a valid email but leave password empty
        Espresso.onView(withId(R.id.login_email)).perform(ViewActions.typeText("validemail@example.com"));
        Espresso.onView(withId(R.id.login_button)).perform(click());

        // Check if the error message for empty password is shown
        Espresso.onView(withId(R.id.login_password))
                .check(matches(hasErrorText("Password cannot be empty")));
    }

    @Test
    public void testLoginWithValidCredentials() {
        // Enter valid email and password
        Espresso.onView(withId(R.id.login_email)).perform(ViewActions.typeText("validemail@example.com"));
        Espresso.onView(withId(R.id.login_password)).perform(ViewActions.typeText("validpassword"));
        Espresso.onView(withId(R.id.login_button)).perform(click());

        // Check if login is successful (after login, MainActivity should be opened)
        Espresso.onView(withId(R.id.main_activity_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testRedirectToSignUp() {
        // Simulate click on Sign-Up redirect text
        Espresso.onView(withId(R.id.signUpRedirectText)).perform(click());

        // Check if the SignUpActivity is opened
        Espresso.onView(ViewMatchers.withId(R.id.signUpActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testRedirectToForgotPassword() {
        // Simulate click on Forgot Password text
        Espresso.onView(withId(R.id.forgot_password)).perform(click());

        // Check if the ForgotPassword dialog or activity is shown
        Espresso.onView(ViewMatchers.withId(R.id.forgot_password)).check(matches(isDisplayed()));
    }

    // Optional: You can also add a test for Google Sign-In if needed
    // Example for Google Sign-In (Note: You'll need to mock Google Sign-In here)
    @Test
    public void testGoogleSignIn() {
        // Click Google Sign-In button
        Espresso.onView(withId(R.id.googleBtn)).perform(click());

        // Check if Google Sign-In Intent is fired (you might need to mock Google SignIn)
        // Test might be implemented in a more complex way to mock and verify Google Sign-In behavior
    }
}
