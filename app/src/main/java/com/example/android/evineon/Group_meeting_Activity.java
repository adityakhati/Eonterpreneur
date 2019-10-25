package com.example.android.evineon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import Group_Activities.Group_Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Group_meeting_Activity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDate;
    int day,month,year;
    private int hr;
    private int min;
    static final int TIME_DIALOG_ID = 1111;
    private String dateString,time_str,subject_str="",reason_str,loc_str,groupname;

    EditText edsub,edreason,edloc;
    Button btndate,btntime,btnconfirm;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_meeting);

        Intent i=getIntent();
        groupname=i.getStringExtra("Groupname");

        edsub=(EditText)findViewById(R.id.input_subject);
        edloc=(EditText)findViewById(R.id.input_location);
        edreason=(EditText)findViewById(R.id.input_reason);

        btndate=(Button)findViewById(R.id.btn_date);
        btntime=(Button)findViewById(R.id.btn_time);
        btnconfirm=(Button)findViewById(R.id.btn_confirm);
        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdDialog(0).show();
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject_str=edsub.getText().toString();
                loc_str=edloc.getText().toString();
                reason_str=edreason.getText().toString();

                if(time_str!=null || dateString!=null || subject_str.equals("")) {
                    auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    final String uid = user.getUid();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference();

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String x;
                            int i;
                            for (i = 1; ; i++) {
                                x = Integer.toString(i);
                                if (!dataSnapshot.child("Groups").child(groupname).child("Meeting").hasChild(x)) {
                                    break;
                                }
                            }
                            myRef.child("Groups").child(groupname).child("Meeting").child(x).child("date").setValue(dateString);
                            myRef.child("Groups").child(groupname).child("Meeting").child(x).child("time").setValue(time_str);
                            myRef.child("Groups").child(groupname).child("Meeting").child(x).child("location").setValue(loc_str);
                            myRef.child("Groups").child(groupname).child("Meeting").child(x).child("subject").setValue(subject_str);
                            myRef.child("Groups").child(groupname).child("Meeting").child(x).child("reason").setValue(reason_str);
                            myRef.child("Groups").child(groupname).child("Meeting").child(x).child("by").setValue(uid);

                            startActivity(new Intent(Group_meeting_Activity.this, Group_Activity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    if(time_str==null)
                        btntime.setError("Fill");
                    if(dateString==null)
                        btntime.setError("Fill");
                    if(subject_str.equals(""))
                        btntime.setError("Fill");

                }


            }
        });

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                /*DatePickerDialog dialog = new DatePickerDialog(Schedule_Meeting_Activity.this,
                        R.style.AppTheme, mDate,
                        year, month, day);*/
                DatePickerDialog dialog=new DatePickerDialog(Group_meeting_Activity.this,
                        R.style.AppTheme_Dark_Dialog,mDate,
                        year,month,day);

                String color_string = "#303030";
                int myColor = Color.parseColor(color_string);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(myColor));
                dialog.show();
            }
        });


        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                dateString = dayOfMonth + "/" + month + "/" + year;
                btndate.setText(dateString);
            }
        };
        
    }

    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timePickerListener, hr, min, false);
        }
        return new TimePickerDialog(this, timePickerListener, hr, min, false);
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min);
        }
    };


    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        time_str = new StringBuilder().append(hr).append(':').append(min).append(" ").append(timeSet).toString();
        btntime.setText(time_str);
    }
}
