package Account_Activities;

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

import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Add_Account_Activity extends AppCompatActivity {
    
    EditText edloc,edamt,edreason;
    Button eddate;
    String loc,date,amt,reason,dateString,rbgender_str;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;

    String groupname;
    int i;

    RadioGroup rggender;
    RadioButton rggende_bt;

    private DatePickerDialog.OnDateSetListener mDate;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        
        edamt=(EditText)findViewById(R.id.input_amt);
        eddate=(Button) findViewById(R.id.input_dob);
        edloc=(EditText)findViewById(R.id.input_location);
        edreason=(EditText)findViewById(R.id.input_reason);
        rggender = (RadioGroup)findViewById(R.id.radio_gender);



        eddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Add_Account_Activity.this,
                        R.style.AppTheme_Dark_Dialog, mDate,
                        year, month, day);

                String color_string = "#303030";
                int myColor = Color.parseColor(color_string);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(myColor));
                dialog.show();

                dialog.show();
            }
        });


        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                dateString = dayOfMonth + "/" + month + "/" + year;
                eddate.setText(dateString);
            }
        };


        findViewById(R.id.btn_create_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt=edamt.getText().toString();
                reason=edreason.getText().toString();
                loc=edloc.getText().toString();
                final int rdgenderint = rggender.getCheckedRadioButtonId();
                rggende_bt = (RadioButton) findViewById(rdgenderint);
                rbgender_str = rggende_bt.getText().toString();

                if(amt.equals("") || reason.equals("")){
                    edamt.setError("Fill");
                    edreason.setError("Fill");
                }
                else
                {
                    mAuth = FirebaseAuth.getInstance();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference();

                    FirebaseUser user=mAuth.getCurrentUser();
                    final String uid=user.getUid();

                    final Intent intent=getIntent();
                    groupname=intent.getExtras().getString("Groupname");


                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String x;
                            int i;
                            for(i=1;;i++) {
                                x=Integer.toString(i);
                                if (!dataSnapshot.child("Groups").child(groupname).child("Accounts").hasChild(x)) {
                                    break;
                                }
                            }
                            myRef.child("Groups").child(groupname).child("Accounts").child(x).child("amount").setValue(amt);
                            myRef.child("Groups").child(groupname).child("Accounts").child(x).child("reason").setValue(reason);
                            myRef.child("Groups").child(groupname).child("Accounts").child(x).child("location").setValue(loc);
                            myRef.child("Groups").child(groupname).child("Accounts").child(x).child("date").setValue(dateString);
                            myRef.child("Groups").child(groupname).child("Accounts").child(x).child("madeby").setValue(uid);
                            myRef.child("Groups").child(groupname).child("Accounts").child(x).child("status").setValue(rbgender_str);
                            Intent intent=new Intent(Add_Account_Activity.this, Accounts_Activity.class);
                            intent.putExtra("Groupname",groupname);
                            startActivity(intent);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        
    }
}
