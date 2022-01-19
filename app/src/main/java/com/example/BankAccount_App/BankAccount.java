package com.example.BankAccount_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BankAccount extends AppCompatActivity implements View.OnClickListener {

    double depositAmount, amount, withdrawAmount;
    private TextView balanceText, prevT;
    private EditText depositText, withdrawText;
    private Button depositTextButton, withdrawTextButton, signOUT;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);
        getSupportActionBar().hide();

        balanceText = findViewById(R.id.balance);
        depositText = findViewById(R.id.deposit);
        withdrawText = findViewById(R.id.withdraw);
        prevT = findViewById(R.id.transaction);
        depositTextButton = findViewById(R.id.depositButton);
        depositTextButton.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar4);

        signOUT = findViewById(R.id.logout);
        signOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(BankAccount.this, MainActivity.class));
                progressBar.setVisibility(View.GONE);
            }
        });

        withdrawTextButton = findViewById(R.id.withdrawButton);
        withdrawTextButton.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingText = findViewById(R.id.greeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    greetingText.setText("Welcome " + fullName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.depositButton:
                depositAmount = Double.valueOf(depositText.getText().toString());
                balanceText.setText("$ " + deposit(depositAmount));
                prevT.setText("+ $ " + depositText.getText().toString());
                break;

            case R.id.withdrawButton:
                withdrawAmount = Double.valueOf(withdrawText.getText().toString());
                balanceText.setText("$ " + withdraw(withdrawAmount));
                prevT.setText("- $ " + withdrawText.getText().toString());
                break;
        }
    }

    private double withdraw(double withdrawAmount) {
        amount = amount - withdrawAmount;
        return amount;
    }

    private double deposit(double depositAmount) {
        amount = amount + depositAmount;
        return amount;
    }
}