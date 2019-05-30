package Signing_Activities;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class Addtodatabase_register {


        private static final String TAG = "AddToDatabase";


        //add Firebase Database stuff
        private FirebaseDatabase mFirebaseDatabase;
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private DatabaseReference myRef;

            //declare variables in oncreate

        public Addtodatabase_register(String fname , String lname, String city,String dob, String email,String gender,String type,String phonenumber,String org,String classi){

            //declare the database reference object. This is what we use to access the database.
            //NOTE: Unless you are signed in, this will not be useable.
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        //toastMessage("Successfully signed in with: " + user.getEmail());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                        //toastMessage("Successfully signed out.");
                    }
                    // ...
                }
            };

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Object value = dataSnapshot.getValue();
                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

                   Log.d(TAG, "onClick: Attempting to add object to database.");

                    if (!fname.equals("") && !lname.equals("") && !city.equals("") && !email.equals("") && !dob.equals("")) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();
                        myRef.child("Users").child(userID).child("FirstName").setValue(fname.toLowerCase());
                        myRef.child("Users").child(userID).child("LastName").setValue(lname.toLowerCase());
                        myRef.child("Users").child(userID).child("City").setValue(city.toLowerCase());
                        myRef.child("Users").child(userID).child("Email").setValue(email);
                        myRef.child("Users").child(userID).child("DOB").setValue(dob);
                        myRef.child("Users").child(userID).child("Gender").setValue(gender);
                        myRef.child("Users").child(userID).child("PhoneNumber").setValue(phonenumber);
                        myRef.child("Users").child(userID).child("usertype").setValue("new");
                        myRef.child("Users").child(userID).child("org").setValue(org);
                        myRef.child("Users").child(userID).child("class").setValue(classi);


                        //toastMessage("Adding " + name + " to database...");

                    }
                }


        /*@Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }*/


    }

