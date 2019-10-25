package com.example.android.evineon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Group_meeting_details_Activity extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    private String num,groupname;
    TextView name_tv,date_tv,rea_tv,loc_tv,sub_tv,time_tv;
    private String fname,lname,loc,date,sub,time,reason,fullname,userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_meeting_detials);

        name_tv=(TextView)findViewById(R.id.name);
        loc_tv=(TextView)findViewById(R.id.location);
        date_tv=(TextView)findViewById(R.id.date);
        sub_tv=(TextView)findViewById(R.id.subject);
        rea_tv=(TextView)findViewById(R.id.reason);
        time_tv=(TextView)findViewById(R.id.time);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        final Intent i=getIntent();
        num=i.getExtras().getString("Num");
        groupname=i.getExtras().getString("Groupname");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userid=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("by").getValue().toString();
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                time=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("time").getValue().toString();
                date=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("date").getValue().toString();
                loc=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("location").getValue().toString();
                reason=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("reason").getValue().toString();
                sub=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("subject").getValue().toString();
                fullname=fname+" "+lname;
                name_tv.setText(fullname);
                time_tv.setText(time);
                rea_tv.setText(reason);
                date_tv.setText(date);
                sub_tv.setText(sub);
                loc_tv.setText(loc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
