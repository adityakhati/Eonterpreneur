package Group_Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.evineon.MainActivity;
import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Group_profile_Activity extends AppCompatActivity {

    String name,fname,lname,fullname,activity,userid;
    DatabaseReference myRef,ref;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;
    LinearLayout ll_layout;
    TextView tvname,tvadmin,tvdes;
    Button btn_request,btn_accept,btn_reject;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile_);

        ll_layout=(LinearLayout)findViewById(R.id.ll_layout);

        tvadmin=(TextView)findViewById(R.id.tv_admin);
        tvname=(TextView)findViewById(R.id.tv_name);
        tvdes=(TextView)findViewById(R.id.tv_des);
        //btn_request=(Button)findViewById(R.id.btn_req_join_group);
        btn_request = new Button(Group_profile_Activity.this);
        btn_reject = new Button(Group_profile_Activity.this);
        btn_accept = new Button(Group_profile_Activity.this);

        Intent i=getIntent();
        name=i.getExtras().getString("Name");
        activity=i.getExtras().getString("Activity");

        if(activity.equals("joingrp")) {
            ll_layout.addView(btn_request);
            btn_request.setText("Request To Join Group");
            btn_request.setBackgroundResource(R.drawable.button_rounded);
        }
        else if(activity.equals("reqgrp")) {

            ll_layout.addView(btn_accept);
            btn_accept.setText("Accept Group Request");
            btn_accept.setTextSize(10);
            btn_accept.setBackgroundResource(R.drawable.button_rounded);

            ll_layout.addView(btn_reject);
            btn_reject.setTextSize(10);
            btn_reject.setText("Reject Group Request");
            btn_reject.setBackgroundResource(R.drawable.button_rounded);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 40, 20, 10);
            btn_reject.setLayoutParams(params);
            btn_accept.setLayoutParams(params);
        }



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();



        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String des=dataSnapshot.child("Groups").child(name).child("description").getValue().toString();
                tvname.setText(name);
                tvdes.setText(des);
                myRef.child("Groups").child(name).child("Users").orderByValue().equalTo("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            String userid="";
                            String value = snap.getValue(String.class);
                            userid = snap.getKey();
                            final String uid=userid;
                            if(!userid.equals("")) {
                                mFirebaseDatabase = FirebaseDatabase.getInstance();
                                ref = mFirebaseDatabase.getReference();
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                                        lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                                        fullname = fname +" "+ lname;
                                        Log.d("Fullname",fullname);
                                        tvadmin.setText(fullname);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        auth=FirebaseAuth.getInstance();
        FirebaseUser u=auth.getCurrentUser();
        userid=u.getUid();
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Groups").child(name).child("Incoming Request").child(userid).setValue("Request");
                myRef.child("Users").child(userid).child("Group Requested").child(name).setValue("Waiting");
                startActivity(new Intent(Group_profile_Activity.this,Group_Activity.class));
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Users").child(userid).child("Groupname").setValue(name);
                myRef.child("Groups").child(name).child("Users").child(userid).setValue("new");
                myRef.child("Users").child(userid).child("usertype").setValue("new");
                myRef.child("Users").child(userid).child("Group Requested").removeValue();
                myRef.child("Groups").child(name).child("Incoming Request").child(userid).removeValue();
                startActivity(new Intent(Group_profile_Activity.this, MainActivity.class));
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Users").child(userid).child("Group Requested").child(name).removeValue();
                myRef.child("Groups").child(name).child("Incoming Request").child(userid).removeValue();
                startActivity(new Intent(Group_profile_Activity.this, Group_Activity.class));
            }
        });
    }
}
