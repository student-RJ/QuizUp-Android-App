package com.example.login4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText, passwordStrengthFeedback;
    private ImageButton eyeButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize views
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        eyeButton = findViewById(R.id.eye_button);
        passwordStrengthFeedback = findViewById(R.id.password_strength_feedback); // TextView to display feedback

        // Toggle password visibility when the eye icon is clicked
        eyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Hide password
                    signupPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeButton.setImageResource(R.drawable.eye);  // Set to eye closed icon
                } else {
                    // Show password
                    signupPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                    eyeButton.setImageResource(R.drawable.eye);  // Set to eye open icon
                }
                isPasswordVisible = !isPasswordVisible;  // Toggle the password visibility state
            }
        });

        // Password strength checker (TextWatcher)
        signupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Get the current password
                String password = signupPassword.getText().toString();

                // Check password strength and display feedback
                String strength = getPasswordStrength(password);
                updatePasswordStrengthFeedback(strength);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Handle sign-up button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                // Validate user input
                if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                    return;
                }
                if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            if (firebaseUser != null) {
                                // Send verification email
                                firebaseUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "Verification link sent on your email please check your email inbox to verify", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class)); // Redirect to login
                                                    finish(); // Finish SignUpActivity
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Redirect to login page
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    // Method to evaluate password strength
    private String getPasswordStrength(String password) {
        if (password.length() < 6) {
            return "Weak";
        } else if (password.length() >= 6 && password.length() < 10) {
            return "Medium";
        } else if (password.length() >= 10 && hasSpecialChars(password)) {
            return "Strong";
        } else {
            return "Medium";
        }
    }

    // Helper method to check if password contains special characters
    private boolean hasSpecialChars(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    // Method to update the UI based on password strength
    private void updatePasswordStrengthFeedback(String strength) {
        if (strength.equals("Weak")) {
            passwordStrengthFeedback.setText("Weak Password");
            passwordStrengthFeedback.setTextColor(getResources().getColor(R.color.red));
        } else if (strength.equals("Medium")) {
            passwordStrengthFeedback.setText("Medium Password");
            passwordStrengthFeedback.setTextColor(getResources().getColor(R.color.orange));
        } else {
            passwordStrengthFeedback.setText("Strong Password");
            passwordStrengthFeedback.setTextColor(getResources().getColor(R.color.green));
        }
    }
}
