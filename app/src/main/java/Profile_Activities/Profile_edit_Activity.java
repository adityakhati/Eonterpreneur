package Profile_Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.evineon.MainActivity;
import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import Signing_Activities.Addtodatabase_register;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Profile_edit_Activity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String uid;

    private EditText inputEmail, inputfname,inputlname,inputLoc,inputphone,inputorg,inputclass;
    String year_str,month_str,day_str,dateString;
    private RadioGroup rggender;
    private RadioButton rbgenderbt;

    private Button mDisplaydate,btnconfirm;
    private DatePickerDialog.OnDateSetListener mDate;
    int day,month,year;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        auth = FirebaseAuth.getInstance();
        FirebaseUser userid=auth.getCurrentUser();
        uid=userid.getUid();


        inputfname = (EditText) findViewById(R.id.input_firstname);
        inputlname = (EditText) findViewById(R.id.input_lastname);
        mDisplaydate = (Button) findViewById(R.id.input_dob);

        inputLoc = (EditText) findViewById(R.id.input_city);
        inputEmail = (EditText) findViewById(R.id.input_email);
        btnconfirm = (Button) findViewById(R.id.btn_confirm);
        inputphone=(EditText) findViewById(R.id.input_phone);
        inputorg=(EditText) findViewById(R.id.input_org);
        inputclass=(EditText) findViewById(R.id.input_class);


        rggender = (RadioGroup) findViewById(R.id.radio_gender);

        loaddata();

        mDisplaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Profile_edit_Activity.this,
                        R.style.AppTheme_Dark_Dialog, mDate,
                        year, month, day);

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
                mDisplaydate.setText(dateString);
            }
        };

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fname = inputfname.getText().toString();
                final String city = inputLoc.getText().toString();
                final String email = inputEmail.getText().toString();
                final String lname = inputlname.getText().toString();
                final String inputnum = inputphone.getText().toString();
                final String classi = inputclass.getText().toString();
                final String org = inputorg.getText().toString();


                final int rdgenderint = rggender.getCheckedRadioButtonId();
                rbgenderbt = (RadioButton) findViewById(rdgenderint);
                String rbgender_str = rbgenderbt.getText().toString();
                String rbusertype_str;

                    if (!fname.equals("") && !lname.equals("") && !city.equals("") && !email.equals("") && !inputnum.equals("") && !dateString.equals("")) {
                        new Addtodatabase_register(fname, lname, city, dateString, email, rbgender_str, rbusertype_str = null, inputnum,org,classi);
                        Intent intent = new Intent(Profile_edit_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(
                                Profile_edit_Activity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }

                }
        });
    }
    
    
    private void loaddata(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname=dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                String lname=dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                String email=dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();
                String gender=dataSnapshot.child("Users").child(uid).child("Gender").getValue().toString();
                String dob=dataSnapshot.child("Users").child(uid).child("DOB").getValue().toString();
                String phonenum=dataSnapshot.child("Users").child(uid).child("PhoneNumber").getValue().toString();
                String city=dataSnapshot.child("Users").child(uid).child("City").getValue().toString();
                String classstr=dataSnapshot.child("Users").child(uid).child("class").getValue().toString();
                String orgstr=dataSnapshot.child("Users").child(uid).child("org").getValue().toString();
                mDisplaydate.setText(dob);
                inputEmail.setText(email);
                inputfname.setText(fname);
                inputlname.setText(lname);
                inputphone.setText(phonenum);
                inputLoc.setText(city);
                inputclass.setText(classstr);
                inputorg.setText(orgstr);
                dateString=dob;

                if(gender.equals("Male"))
                    rggender.check(R.id.radio_gender_male);
                else
                    rggender.check(R.id.radio_gender_female);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
