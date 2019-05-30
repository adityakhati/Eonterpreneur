package Account_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Account_details_Activity extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    private String num,groupname;
    TextView name_tv,date_tv,amt_tv,loc_tv,des_tv;
    private String fname,lname,loc,date,amt,des,fullname,userid;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        name_tv=(TextView)findViewById(R.id.name);
        loc_tv=(TextView)findViewById(R.id.location);
        date_tv=(TextView)findViewById(R.id.date);
        des_tv=(TextView)findViewById(R.id.description);
        amt_tv=(TextView)findViewById(R.id.amt);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        final Intent i=getIntent();
        num=i.getExtras().getString("Num");
        groupname=i.getExtras().getString("Groupname");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userid=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("madeby").getValue().toString();
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                amt=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("amount").getValue().toString();
                date=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("date").getValue().toString();
                loc=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("location").getValue().toString();
                des=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("reason").getValue().toString();

                fullname=fname+" "+lname;
                name_tv.setText(fullname);
                des_tv.setText(des);
                date_tv.setText(date);
                amt_tv.setText(amt);
                loc_tv.setText(loc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
