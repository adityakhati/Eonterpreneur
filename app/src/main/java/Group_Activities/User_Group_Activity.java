package Group_Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class User_Group_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Group_User_adapter adapter;
    private List<Group_Users> results;

    Button btn_sec,btn_tre,btn_admin,btn_new;

    private EditText ed_search;
    private String query_str,activity,groupname,usertype;

    DatabaseReference myRef,ref;
    TextView tv_heading;
    FirebaseDatabase mFirebaseDatabase;

    String fname,lname,email,fullname;


    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);


        Intent i=getIntent();
        groupname=i.getExtras().getString("Groupname");
        usertype=i.getExtras().getString("Usertype");

        btn_admin=(Button)findViewById(R.id.btn_admin);
        btn_sec=(Button)findViewById(R.id.btn_sec);
        btn_tre=(Button)findViewById(R.id.btn_tre);
        btn_new=(Button)findViewById(R.id.btn_new);
        tv_heading=(TextView)findViewById(R.id.tv_heading);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbArtists = FirebaseDatabase.getInstance().getReference();

        results = new ArrayList<Group_Users>();

        adapter = new Group_User_adapter(this, results, activity,groupname,usertype);

        recyclerView.setAdapter(adapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        btn_admin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_heading.setText("Admin");
                    myRef.child("Groups").child(groupname).child("Users").orderByValue().equalTo("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            results.clear();
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                String userid = "";
                                userid = snap.getKey();
                                final String uid = userid;
                                if (!userid.equals("")) {
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    ref = mFirebaseDatabase.getReference();
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                                            lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                                            email = dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();
                                            Log.d("Admin", email);

                                            fullname = fname + " " + lname;
                                            results.add(new Group_Users(uid, fullname, email));
                                            adapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                btn_new.setPressed(false);
                btn_sec.setPressed(false);
                btn_tre.setPressed(false);
                btn_admin.setPressed(true);
                return true;
            }
        });

        btn_tre.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_heading.setText("Treasurer");
                    myRef.child("Groups").child(groupname).child("Users").orderByValue().equalTo("treasurer").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            results.clear();
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                String userid = "";
                                String value = snap.getValue(String.class);
                                userid = snap.getKey();
                                final String uid = userid;
                                if (!userid.equals("")) {
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    ref = mFirebaseDatabase.getReference();
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                                            lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                                            email = dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();

                                            fullname = fname + " " + lname;
                                            //res_tre.add(new Treasurer(uid,fullname,email));
                                            results.add(new Group_Users(uid, fullname, email));
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }

                btn_new.setPressed(false);
                btn_sec.setPressed(false);
                btn_tre.setPressed(true);
                btn_admin.setPressed(false);
                return true;
            }
        });

        btn_sec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_heading.setText("Secretary");
                    myRef.child("Groups").child(groupname).child("Users").orderByValue().equalTo("secretary").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            results.clear();
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                String userid = "";
                                String value = snap.getValue(String.class);
                                userid = snap.getKey();
                                final String uid = userid;
                                if (!userid.equals("")) {
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    ref = mFirebaseDatabase.getReference();
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                                            lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                                            email = dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();

                                            fullname = fname + " " + lname;
                                            //res_sec.add(new Secratary(uid,fullname,email));
                                            results.add(new Group_Users(uid, fullname, email));
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }
                btn_new.setPressed(false);
                btn_sec.setPressed(true);
                btn_tre.setPressed(false);
                btn_admin.setPressed(false);
                return true;
            }
        });

        btn_new.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_heading.setText("New Users");
                    myRef.child("Groups").child(groupname).child("Users").orderByValue().equalTo("new").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            results.clear();
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                String userid = "";
                                String value = snap.getValue(String.class);
                                userid = snap.getKey();
                                final String uid = userid;
                                if (!userid.equals("")) {
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    ref = mFirebaseDatabase.getReference();
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                                            lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                                            email = dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();

                                            fullname = fname + " " + lname;
                                            results.add(new Group_Users(uid, fullname, email));
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }

                btn_new.setPressed(true);
                btn_sec.setPressed(false);
                btn_tre.setPressed(false);
                btn_admin.setPressed(false);
                return true;
            }
        });

    }
}
