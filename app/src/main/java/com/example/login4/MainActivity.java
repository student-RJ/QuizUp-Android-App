package com.example.login4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView userName;
    private Button logout;
    private ImageView profilePicture; // Added profile picture
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Firebase Firestore instance

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<QuizModel> quizModelList;
    private QuizListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        userName = findViewById(R.id.userName);
        logout = findViewById(R.id.logout);
        profilePicture = findViewById(R.id.profilePicture); // Initialize the profile picture view
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        // Check for the signed-in user and update UI
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, load their profile data from Firestore
            loadUserProfile(currentUser.getUid());
        } else {
            // If not logged in, show Google account name if available
            GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (gAccount != null) {
                userName.setText(gAccount.getDisplayName());
            }
        }

        // Set up the logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(MainActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()).signOut()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }
                        });
            }
        });

        // Set up profile picture click listener
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity when profile picture is clicked
                openProfileActivity();
            }
        });

        // Initialize RecyclerView and data
        quizModelList = new ArrayList<>();
        adapter = new QuizListAdapter(quizModelList);
        progressBar.setVisibility(View.GONE);  // Hide progress bar initially
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load data from Firebase
        getDataFromFirebase();
    }

    // Method to open ProfileActivity
    private void openProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // Fetch data from Firebase and shuffle
    private void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);  // Show progress bar during data load
        FirebaseDatabase.getInstance().getReference()
                .get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                QuizModel quizModel = snapshot.getValue(QuizModel.class);
                                if (quizModel != null) {
                                    quizModelList.add(quizModel);  // Add data to list
                                }
                            }

                            // Shuffle the data list
                            Collections.shuffle(quizModelList);  // Shuffle the list here
                        }
                        setupRecyclerView();  // Call method to set up RecyclerView after shuffling
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error here
                    progressBar.setVisibility(View.GONE);
                    // Optionally, show a message or log the error
                });
    }

    // Set up the RecyclerView again after fetching data
    private void setupRecyclerView() {
        progressBar.setVisibility(View.GONE);  // Hide progress bar when data is loaded
        adapter.notifyDataSetChanged();  // Notify the adapter about data changes
    }

    // Method to load user profile data from Firestore
    private void loadUserProfile(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                String imageUrl = documentSnapshot.getString("imageUrl");

                                // Update the username in the UI
                                if (name != null) {
                                    userName.setText(name);
                                }

                                // Load profile image using Glide (if available)
                                if (imageUrl != null) {
                                    Glide.with(MainActivity.this).load(imageUrl).into(profilePicture);
                                }
                            } else {
                                // Handle case when profile doesn't exist
                                userName.setText("Guest User");
                            }
                        } else {
                            // Task failed
                            userName.setText("Error loading user data");
                        }
                    }
                });
    }
}
