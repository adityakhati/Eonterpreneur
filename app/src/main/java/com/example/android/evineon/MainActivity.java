package com.example.android.evineon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Connection.Connection_Activity;
import Group_Activities.Group_Activity;
import Profile_Activities.Profile_Activity;
import Schedule_meeting.MeetingActivity;
import Search_activities.SearchActivity;
import Signing_Activities.LoginActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user;
    String userType;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(MainActivity.this);

        setContentView(R.layout.activity_main);



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        // Read from the database
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(valueEventListener);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        //Profile
        findViewById(R.id.card_view_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Profile_Activity.class));
            }
        });

        //Search
        findViewById(R.id.card_view_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("Search","User");
                startActivity(intent);

/*
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
*/

            }
        });

        findViewById(R.id.card_view_orders_past).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MeetingActivity.class));

            }
        });

        findViewById(R.id.card_view_orders_pending).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Connection_Activity.class));
            }
        });

        findViewById(R.id.card_view_groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Group_Activity.class));
            }
        });

        findViewById(R.id.card_view_tyfcb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TYFCB_Activity.class);
                intent.putExtra("Activity","main");
                startActivity(intent);

            }
        });

        findViewById(R.id.card_view_ref).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Referral_Activity.class);
                intent.putExtra("Activity","main");
                startActivity(intent);
            }
        });







/*
        findViewById(R.id.card_view_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = getResources().getConfiguration().locale;
                Log.d("Hiiiiiii", locale.getDisplayLanguage());
                if (locale.getDisplayLanguage().equals("English")){
                    setLocale("hi");
                } else {
                    setLocale("en");
                }
            }
        });
*/

        //LogOut
        findViewById(R.id.card_view_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

                // this listener will be called when there is change in firebase user session
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
/*

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();



        // Read from the database
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(valueEventListener);
*/

//        loaddata();
    }

    @Override
    protected void onResume() {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //List<ParentObject> parentObject = new ArrayList<>();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            if(user!=null) {

                String id = user.getUid();

                if (dataSnapshot.exists()) {
                    /*userType = dataSnapshot.child("Users").child(id).child("UserType").getValue().toString();
                    if(userType.equals("Farmer"))
                    {
                        startActivity(new Intent(MainActivity.this, DashBoardFarmerActivity.class));
                        finish();
                    }*/
                }
            }
            else
                Log.d("Aditya", "null user");

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
