package Profile_Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.evineon.MainActivity;
import com.example.android.evineon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import Group_Activities.Group_Activity;
import Group_Activities.Group_profile_Activity;
import Schedule_meeting.Schedule_Meeting_Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class New_Profile_Activity extends AppCompatActivity {
    ImageView img_profile_pic;
    Button connect, request_bt, accept_bt, reject_bt, meet_bt;
    TextView tvname, tvemail, tvgender, tvdob, tvphonenuber, tvmeet;
    private String email, dob, gender, phonenum, fullname, fname, lname, uid, userid, status,status_meet,date_meet, activity, url,time_meet;
    private int flag=0;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private LinearLayout parentLinearLayout;


    private void ShowImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference ref = storage.getReference();

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Intent intent = getIntent();
        userid = intent.getStringExtra("Uid");


        String imgref = "Users/" + userid + "/Profile_pic";
        userid = intent.getStringExtra("Uid");


        ref.child(imgref).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                url = uri.toString();
                Picasso.get()
                        .load(url)
                        .into(img_profile_pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void takedata() {
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        uid = user.getUid();


        Intent intent = getIntent();
        userid = intent.getStringExtra("Uid");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname = dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname = dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                email = dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                gender = dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                dob = dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                phonenum = dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
                if (dataSnapshot.child("Connection").child(uid).hasChild(userid))//childe(userid))
                {
                    status = dataSnapshot.child("Connection").child(uid).child(userid).getValue().toString();
                    Log.d("STATUS SEARCH ADAPTER", status);
                }
                fullname = fname + " " + lname;
                tvdob.setText(dob);
                tvemail.setText(email);
                tvname.setText(fullname);
                tvgender.setText(gender);
                tvphonenuber.setText(phonenum);
                if(status==null)
                    status="";

                datawork();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void takedata_meeting() {
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        uid = user.getUid();


        Intent intent = getIntent();
        userid = intent.getStringExtra("Uid");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname = dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname = dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                email = dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                gender = dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                dob = dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                phonenum = dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
                /*if (dataSnapshot.child("Connection").child(uid).hasChild(userid))//childe(userid))
                {
                    status = dataSnapshot.child("Connection").child(uid).child(userid).getValue().toString();
                    Log.d("STATUS SEARCH ADAPTER", status);
                }*/
                status_meet = dataSnapshot.child("Meeting").child(userid).child(uid).child("status").getValue().toString();
                date_meet = dataSnapshot.child("Meeting").child(userid).child(uid).child("date").getValue().toString();
                time_meet = dataSnapshot.child("Meeting").child(userid).child(uid).child("time").getValue().toString();
                fullname = fname + " " + lname;
                tvdob.setText(date_meet);
                tvemail.setText(email);
                tvname.setText(fullname);
                tvgender.setText(gender);
                tvphonenuber.setText(phonenum);

                datawork();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Meeting_btn(){
        meet_bt = new Button(New_Profile_Activity.this);
        parentLinearLayout.addView(meet_bt);
        meet_bt.setBackgroundResource(R.drawable.button_rounded);
        meet_bt.setText("Schedule Meeting");
        meet_bt.setWidth(200);
        meet_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(New_Profile_Activity.this, Schedule_Meeting_Activity.class);
                intent.putExtra("UID",userid);
                startActivity(intent);
            }
        });
    }


    private void datawork() {
        if(uid.equals(userid))
        {

        }
        else {
        if (activity.equals("Search")) {

            if (status.equals("Accepted")) {
                Meeting_btn();

            } else {
                request_bt = new Button(New_Profile_Activity.this);
                parentLinearLayout.addView(request_bt);
                request_bt.setText("Request");
                request_bt.setBackgroundResource(R.drawable.button_rounded);

                request_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        myRef = mFirebaseDatabase.getReference();

                        myRef.child("Connection").child(userid).child(uid).setValue("Requested");
                        myRef.child("Connection").child(uid).child(userid).setValue("Request");
                        Toast.makeText(New_Profile_Activity.this, "Requested", Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }
        else if(activity.equals("Incomingreq")){
            accept_bt = new Button(New_Profile_Activity.this);
            parentLinearLayout.addView(accept_bt);

            reject_bt = new Button(New_Profile_Activity.this);
            parentLinearLayout.addView(reject_bt);

            reject_bt.setWidth(200);
            reject_bt.setBackgroundResource(R.drawable.button_rounded);
            reject_bt.setText("Reject");
            accept_bt.setBackgroundResource(R.drawable.button_rounded);
            accept_bt.setWidth(200);
            accept_bt.setText("Accept");

            Intent i=getIntent();
            final String grpname = i.getStringExtra("Groupname");


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 40, 20, 10);
            accept_bt.setLayoutParams(params);
            reject_bt.setLayoutParams(params);

            accept_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("Users").child(userid).child("Groupname").setValue(grpname);
                    myRef.child("Groups").child(grpname).child("Users").child(userid).setValue("new");
                    myRef.child("Users").child(userid).child("usertype").setValue("new");
                    myRef.child("Groups").child(grpname).child("Incoming Request").child(userid).removeValue();
                    myRef.child("Users").child(userid).child("Group Requested").removeValue();
                    startActivity(new Intent(New_Profile_Activity.this, MainActivity.class));

                }
            });

            reject_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("Users").child(userid).child("Group Requested").child(grpname).removeValue();
                    myRef.child("Groups").child(grpname).child("Incoming Request").child(userid).removeValue();
                    startActivity(new Intent(New_Profile_Activity.this, Group_Activity.class));
                }
            });
        }
            else if (activity.equals("Meeting")) {

            takedata_meeting();


            if (status_meet.equals("A")) {
                if(flag==0) {
                    //tvmeet = new TextView(New_Profile_Activity.this);
                    TextView tvtime= new TextView(New_Profile_Activity.this);
                    //parentLinearLayout.addView(tvmeet);
                    parentLinearLayout.addView(tvtime);
                    tvtime.setTextSize(25);
                    tvtime.setGravity(Gravity.CENTER);
                    ///tvmeet.setTextSize(25);
                    //tvdob.setText(date_meet);
                    //tvmeet.setText(date_meet);
                    tvtime.setText(time_meet);
                    flag=1;
                }

            } else if (status_meet.equals("W")) {

                if(flag==0) {
                    tvmeet = new TextView(New_Profile_Activity.this);
                    parentLinearLayout.addView(tvmeet);
                    tvmeet.setTextSize(25);
                    tvmeet.setGravity(Gravity.CENTER);
                    tvmeet.setText("Waiting For Conformation");
                    flag = 1;
                }


            } else {

                if(flag==0) {
                    accept_bt = new Button(New_Profile_Activity.this);
                    parentLinearLayout.addView(accept_bt);

                    reject_bt = new Button(New_Profile_Activity.this);
                    parentLinearLayout.addView(reject_bt);

                    reject_bt.setWidth(200);
                    reject_bt.setBackgroundResource(R.drawable.button_rounded);
                    reject_bt.setText("Reject");
                    accept_bt.setBackgroundResource(R.drawable.button_rounded);
                    accept_bt.setWidth(200);
                    accept_bt.setText("Accept");

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(20, 40, 20, 10);
                    accept_bt.setLayoutParams(params);
                    reject_bt.setLayoutParams(params);

                    reject_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                            myRef = mFirebaseDatabase.getReference();

                   /* myRef.child("Connection").child(userid).child(uid)setValue("Rejected");
                    myRef.child("Connection").child(uid).child(userid).setValue("Rejected");
                   */
                            myRef.child("Connection").child(userid).child(uid).removeValue();
                            myRef.child("Connection").child(uid).child(userid).removeValue();
                            Toast.makeText(New_Profile_Activity.this, "Rejected", Toast.LENGTH_SHORT).show();
                        }
                    });

                    accept_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                            myRef = mFirebaseDatabase.getReference();

                            myRef.child("Meeting").child(userid).child(uid).child("status").setValue("A");
                            myRef.child("Meeting").child(uid).child(userid).child("status").setValue("A");
                            Toast.makeText(New_Profile_Activity.this, "Accepted", Toast.LENGTH_SHORT).show();

                        }
                    });
                    flag=1;
                }
            }
        }
        else if (activity.equals("Connection")) {
            Log.d("New profile Status", status);

            if (status.equals("Request")) {

            }
            else if (status.equals("Accepted")) {

            }
            else {
                accept_bt = new Button(New_Profile_Activity.this);
                parentLinearLayout.addView(accept_bt);

                reject_bt = new Button(New_Profile_Activity.this);
                parentLinearLayout.addView(reject_bt);

                reject_bt.setWidth(200);
                reject_bt.setText("Reject");
                accept_bt.setWidth(200);
                accept_bt.setText("Accept");


                reject_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        myRef = mFirebaseDatabase.getReference();

                   /* myRef.child("Connection").child(userid).child(uid)setValue("Rejected");
                    myRef.child("Connection").child(uid).child(userid).setValue("Rejected");
                   */
                        myRef.child("Connection").child(userid).child(uid).removeValue();
                        myRef.child("Connection").child(uid).child(userid).removeValue();
                        Toast.makeText(New_Profile_Activity.this, "Rejected", Toast.LENGTH_SHORT).show();
                    }
                });

                accept_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        myRef = mFirebaseDatabase.getReference();

                        myRef.child("Connection").child(userid).child(uid).setValue("Accepted");
                        myRef.child("Connection").child(uid).child(userid).setValue("Accepted");
                        Toast.makeText(New_Profile_Activity.this, "Accepted", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__profille);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_ll);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        img_profile_pic = (ImageView) findViewById(R.id.img_profile_pic);
        tvdob = (TextView) findViewById(R.id.dob);
        tvemail = (TextView) findViewById(R.id.email);
        tvname = (TextView) findViewById(R.id.name);
        tvgender = (TextView) findViewById(R.id.gender);
        tvphonenuber = (TextView) findViewById(R.id.phonenumber);

        Intent intent = getIntent();
        activity = intent.getStringExtra("Activity");

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        final String userID = user.getUid();
        if(activity.equals("Meeting"))
            takedata_meeting();
        else
            takedata();

    }

        @Override
        protected void onStart(){
            super.onStart();
            ShowImage();
        }
}


