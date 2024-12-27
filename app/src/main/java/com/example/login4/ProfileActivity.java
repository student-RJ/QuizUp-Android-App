package com.example.login4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.provider.MediaStore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private TextView profileName;
    private EditText editTextName, editTextEmail, editTextDob, editTextNumber, editTextLocation, editTextNationality;
    private Button saveButton, logoutButton;
    private ImageView profileImage;
    private ProgressBar progressBar;
    private Switch themeSwitch, notificationSwitch;
    private Spinner spinnerGender;  // Spinner for gender selection

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initialize views
        profileName = findViewById(R.id.profileName);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDob = findViewById(R.id.editTextDob);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextNationality = findViewById(R.id.editTextNationality);
        saveButton = findViewById(R.id.saveButton);
        profileImage = findViewById(R.id.profileImage);
        progressBar = findViewById(R.id.progressBar);
        themeSwitch = findViewById(R.id.themeSwitch);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        logoutButton = findViewById(R.id.logoutButton);
        spinnerGender = findViewById(R.id.genderSpinner);  // Spinner for gender

        // Set up the Spinner for gender
        Spinner genderSpinner = findViewById(R.id.genderSpinner);

// Populate Spinner with gender options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set adapter to Spinner
        genderSpinner.setAdapter(adapter);

// Optional: Set default selection or handle selected item
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = parent.getItemAtPosition(position).toString();
                // Handle selection
                if (selectedGender.equals("Select Gender")) {
                    // You can show a warning or do nothing
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle no selection
            }
        });


        // Load the user profile from Firestore
        loadUserProfile();

        // Open image picker when profile image is clicked
        profileImage.setOnClickListener(v -> openImagePicker());

        // DatePicker for DOB field
        editTextDob.setOnClickListener(v -> showDatePickerDialog());

        // Save button click event
        saveButton.setOnClickListener(view -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String dob = editTextDob.getText().toString().trim();
            String number = editTextNumber.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();  // Get selected gender
            String location = editTextLocation.getText().toString().trim();
            String nationality = editTextNationality.getText().toString().trim();

            if (validateInputs(name, email, dob, number, gender, location, nationality)) {
                saveData(name, email, dob, number, gender, location, nationality);
            }
        });

        // Logout button functionality
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Theme switch toggle
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setTheme(R.style.AppTheme_Dark);
            } else {
                setTheme(R.style.AppTheme_Light);
            }
        });

        // Notification switch toggle
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(ProfileActivity.this, isChecked ? "Notifications Enabled" : "Notifications Disabled", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve and populate user data from Firestore
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String dob = documentSnapshot.getString("dob");
                            String number = documentSnapshot.getString("number");
                            String gender = documentSnapshot.getString("gender");
                            String location = documentSnapshot.getString("location");
                            String nationality = documentSnapshot.getString("nationality");
                            String imageUrl = documentSnapshot.getString("imageUrl");

                            // Set the values to the profile UI elements
                            if (name != null) profileName.setText(name);
                            if (email != null) editTextEmail.setText(email);
                            if (dob != null) editTextDob.setText(dob);
                            if (number != null) editTextNumber.setText(number);
                            if (gender != null) {
                                // Set the selected gender in the spinner
                                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerGender.getAdapter();
                                int position = adapter.getPosition(gender);
                                spinnerGender.setSelection(position);
                            }
                            if (location != null) editTextLocation.setText(location);
                            if (nationality != null) editTextNationality.setText(nationality);
                            if (imageUrl != null)
                                Glide.with(ProfileActivity.this).load(imageUrl).into(profileImage);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show());
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private boolean validateInputs (String name, String email, String dob, String number, String
        gender, String location, String nationality){
            if (TextUtils.isEmpty(name)) {
                editTextName.setError("Name is required");
                return false;
            }
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Enter a valid email address");
                return false;
            }
            if (TextUtils.isEmpty(dob)) {
                editTextDob.setError("Date of Birth is required");
                return false;
            }
            if (TextUtils.isEmpty(number)) {
                editTextNumber.setError("Number is required");
                return false;
            }
            if (TextUtils.isEmpty(gender) || gender.equals("Select Gender")) {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(location)) {
                editTextLocation.setError("Location is required");
                return false;
            }
            if (TextUtils.isEmpty(nationality)) {
                editTextNationality.setError("Nationality is required");
                return false;
            }
            return true;
        }

    private void saveData(String name, String email, String dob, String number, String gender, String location, String nationality) {
        progressBar.setVisibility(View.VISIBLE);

        // Create a map of data to store (not including the image URL for Firestore)
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("dob", dob);
        userData.put("number", number);
        userData.put("gender", gender);
        userData.put("location", location);
        userData.put("nationality", nationality);

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Check if image is selected for uploading
            if (selectedImageUri != null) {
                // Upload image to Firebase Storage
                StorageReference profileImageRef = storageReference.child("profile_images/" + currentUser.getUid() + ".jpg");

                profileImageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the image URL after upload (but don't save it to Firestore)
                            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Show the image URL as a Toast to the user
                                String imageUrl = uri.toString();
                                Toast.makeText(ProfileActivity.this, "Profile image uploaded successfully: " + imageUrl, Toast.LENGTH_LONG).show();
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // If no image is selected, show a message to the user
                Toast.makeText(ProfileActivity.this, "No profile image selected", Toast.LENGTH_SHORT).show();
            }

            // Save other user data to Firestore (without image URL)
            saveUserDataToFirestore(userData, currentUser.getUid());
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ProfileActivity.this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveUserDataToFirestore (Map < String, Object > userData, String userId){
            // Save user data to Firestore
            db.collection("users").document(userId).set(userData)
                    .addOnSuccessListener(aVoid -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    });
        }

        private void showDatePickerDialog () {
            // Get current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Format selected date as dd/MM/yyyy
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                editTextDob.setText(dateFormat.format(selectedDate.getTime()));
            }, year, month, day);

            datePickerDialog.show();
        }
    }
