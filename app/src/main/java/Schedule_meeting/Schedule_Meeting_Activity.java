package Schedule_meeting;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.evineon.MainActivity;
import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class Schedule_Meeting_Activity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDate;
    int day,month,year;
    String dateString,time_str;
    Button requestbt,datebt;
    Button time_btn;
    String userid;

    private int hr;
    private int min;
    static final int TIME_DIALOG_ID = 1111;


    private TextView settimeview;
    private EditText loc_ed,des_ed;


    private int hours_int=25,min_int=61;
    private int flag=0;



    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__meeting);

        requestbt=(Button)findViewById(R.id.request_bt);
        datebt=(Button)findViewById(R.id.date_picker_bt);
        loc_ed=(EditText)findViewById(R.id.loc_ed);
        des_ed=(EditText)findViewById(R.id.des_ed);


        Intent intent=getIntent();
        userid=intent.getStringExtra("UID");



        time_btn=(Button)findViewById(R.id.time_picker_bt);
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdDialog(0).show();
            }
        });

        datebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                /*DatePickerDialog dialog = new DatePickerDialog(Schedule_Meeting_Activity.this,
                        R.style.AppTheme, mDate,
                        year, month, day);*/
                DatePickerDialog dialog=new DatePickerDialog(Schedule_Meeting_Activity.this,
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
                datebt.setText(dateString);
            }
        };

        requestbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc_str=loc_ed.getText().toString();
                String des_str=des_ed.getText().toString();
                if(dateString==null || time_str==null || loc_str==null)
                {
                    loc_ed.setError("Location not entered");
                    time_btn.setError("Time not set");
                    datebt.setError("Date not set");
                }
                else {
                    auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    String uid = user.getUid();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference();
                    myRef.child("Meeting").child(userid).child(uid).child("date").setValue(dateString);
                    myRef.child("Meeting").child(uid).child(userid).child("date").setValue(dateString);
                    myRef.child("Meeting").child(userid).child(uid).child("time").setValue(time_str);
                    myRef.child("Meeting").child(uid).child(userid).child("time").setValue(time_str);
                    myRef.child("Meeting").child(userid).child(uid).child("location").setValue(loc_str);
                    myRef.child("Meeting").child(uid).child(userid).child("location").setValue(loc_str);
                    myRef.child("Meeting").child(userid).child(uid).child("status").setValue("W");
                    myRef.child("Meeting").child(uid).child(userid).child("status").setValue("R");
                    myRef.child("Meeting").child(userid).child(uid).child("description").setValue(des_str);
                    myRef.child("Meeting").child(uid).child(userid).child("description").setValue(des_str);
                    Toast.makeText(Schedule_Meeting_Activity.this, "Requested", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Schedule_Meeting_Activity.this, MainActivity.class));
                }
            }
        });
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

        hours_int=hours;
        min_int=Integer.parseInt(minutes);



        time_str = new StringBuilder().append(hr).append(':').append(min).append(" ").append(timeSet).toString();
        time_btn.setText(time_str);
    }

}
