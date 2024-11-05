package com.example.login4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections; // Added for shuffling

public class MainActivity extends AppCompatActivity {

    private TextView userName;
    private Button logout;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<QuizModel> quizModelList;
    private QuizListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        userName = findViewById(R.id.userName);
        logout = findViewById(R.id.logout);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        // Setup Google Sign-In
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }

        // Set up the logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
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

        // Initialize RecyclerView and data
        quizModelList = new ArrayList<>();
        adapter = new QuizListAdapter(quizModelList);
        progressBar.setVisibility(View.GONE);  // Hide progress bar initially
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load data from Firebase
        getDataFromFirebase();
    }

    // Fetch data from Firebase and shuffle
    private void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);  // Show progress bar during data load
        FirebaseDatabase.getInstance().getReference()
                .get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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
                });
    }

    // Set up the RecyclerView again after fetching data
    private void setupRecyclerView() {
        progressBar.setVisibility(View.GONE);  // Hide progress bar when data is loaded
        adapter.notifyDataSetChanged();  // Notify the adapter about data changes
    }
}
