package com.example.testapplicationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// IT18233704 N.R Yamasinghe
public class MainActivity extends AppCompatActivity {

    EditText txtID, txtName, txtAddress, txtContactNo;
    Button btnSave, btnShow, btnUpdate, btnDelete;
    DatabaseReference dbRef;
    Student std;

    private void clearControls() {
        txtID.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContactNo.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Hooks
        txtID = findViewById(R.id.id_input);
        txtName = findViewById(R.id.name_input);
        txtAddress = findViewById(R.id.address_input);
        txtContactNo = findViewById(R.id.contactNo_input);


        btnSave = findViewById(R.id.save_btn);
        btnShow = findViewById(R.id.show_btn);
        btnUpdate = findViewById(R.id.update_btn);
        btnDelete = findViewById(R.id.delete_btn);

        std = new Student();


        // save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
                try {
                    if (TextUtils.isEmpty(txtID.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter an ID", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(txtName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(txtAddress.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
                    else {

                        // Take inputs from the user and assigning them in to std instance
                        std.setID(txtID.getText().toString().trim());
                        std.setName(txtName.getText().toString().trim());
                        std.setAddress(txtAddress.getText().toString().trim());
                        std.setConNo(Integer.parseInt(txtContactNo.getText().toString().trim()));

                        // Insert to the database
                        dbRef.push().setValue(std);
//                        dbRef.child("Std1").setValue(std);


                        // Feed to user via a toast
                        Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Show
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Student").child("Std1");
                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            txtID.setText(dataSnapshot.child("id").getValue().toString());
                            txtName.setText(dataSnapshot.child("name").getValue().toString());
                            txtAddress.setText(dataSnapshot.child("address").getValue().toString());
                            txtContactNo.setText(dataSnapshot.child("conNo").getValue().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "No source to display", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


        // Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updRef = FirebaseDatabase.getInstance().getReference().child("Student");
                updRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Std1")) {
                            try {
                                std.setID(txtID.getText().toString().trim());
                                std.setName(txtName.getText().toString().trim());
                                std.setAddress(txtAddress.getText().toString().trim());
                                std.setConNo(Integer.parseInt(txtContactNo.getText().toString().trim()));

                                dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("Std1");
                                dbRef.setValue(std);
                                clearControls();

                                // Feedback to the user via toast
                                Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();


                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No source to update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        // Delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Student");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Std1")) {
                            dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("Std1");
                            dbRef.removeValue();
                            clearControls();
                            Toast.makeText(getApplicationContext(), "Data deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No source to delete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
