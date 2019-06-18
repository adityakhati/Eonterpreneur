package TYFCBandReferral_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.evineon.MainActivity;
import com.example.android.evineon.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TYFCB_Activity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    private EditText inputrs, inputcomment;
    Button btnconfirm;
    Button btn_grp;
    String uid,name,userid;


    private RadioGroup rgref,rgbusi;
    private RadioButton rbref,rbbusi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyfcb);


        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        FirebaseUser u_id=auth.getCurrentUser();
        userid=u_id.getUid();

        btn_grp = (Button) findViewById(R.id.btn_add_grp);
        inputcomment = (EditText) findViewById(R.id.input_comment);
        btnconfirm = (Button) findViewById(R.id.btn_confirm);
        inputrs=(EditText) findViewById(R.id.input_rs);
        rgref = (RadioGroup) findViewById(R.id.rad_reff);
        rgbusi = (RadioGroup) findViewById(R.id.rad_business_type);

        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent=new Intent(TYFCB_Activity.this, Show_Activity.class);
                intent.putExtra("type","given");
                intent.putExtra("act","tyfcb");
                startActivity(intent);

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent=new Intent(TYFCB_Activity.this, Show_Activity.class);
                intent.putExtra("type","rec");
                intent.putExtra("act","tyfcb");
                startActivity(intent);

            }
        });



        Intent i=getIntent();
        String act=i.getExtras().getString("Activity");
        if(act.equals("add"))
        {
            uid=i.getExtras().getString("Uid");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String fname=dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                    String lname=dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                    String fullname=fname+" "+lname;
                    btn_grp.setText(fullname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        btn_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TYFCB_Activity.this, AddSearch_Activity.class);
                intent.putExtra("Activity","TYFCB");
                startActivity(intent);
            }
        });
        if(act.equals("add"))
        {
        }

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment=inputcomment.getText().toString();
                final String rs=inputrs.getText().toString();
                String thank_grp=uid;


                final int rdbusi = rgbusi.getCheckedRadioButtonId();
                rbbusi = (RadioButton) findViewById(rdbusi);
                final String rbbusi_str = rbbusi.getText().toString();

                final int rdref = rgref.getCheckedRadioButtonId();
                rbref = (RadioButton) findViewById(rdref);
                final String rbref_str = rbref.getText().toString();



                if(!rs.equals("") && !thank_grp.equals("")){

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String x,y;
                            int i;
                            for (i = 1; ; i++) {
                                x = Integer.toString(i);
                                if (!dataSnapshot.child("TYFCB").child(userid).child("given").hasChild(x)) {
                                    break;
                                }
                            }
                            for (i = 1; ; i++) {
                                y = Integer.toString(i);
                                if (!dataSnapshot.child("TYFCB").child(userid).child("received").hasChild(y)) {
                                    break;
                                }
                            }

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            Date date = new Date();
                            String date_str=formatter.format(date);


                            myRef.child("TYFCB").child(userid).child("given").child(x).child("Rupees").setValue(rs);
                            myRef.child("TYFCB").child(userid).child("given").child(x).child("Comment").setValue(comment);
                            myRef.child("TYFCB").child(userid).child("given").child(x).child("Business").setValue(rbbusi_str);
                            myRef.child("TYFCB").child(userid).child("given").child(x).child("Referral").setValue(rbref_str);
                            myRef.child("TYFCB").child(userid).child("given").child(x).child("To").setValue(uid);
                            myRef.child("TYFCB").child(userid).child("given").child(x).child("Date").setValue(date_str);

                            myRef.child("TYFCB").child(uid).child("received").child(y).child("Rupees").setValue(rs);
                            myRef.child("TYFCB").child(uid).child("received").child(y).child("Comment").setValue(comment);
                            myRef.child("TYFCB").child(uid).child("received").child(y).child("Business").setValue(rbbusi_str);
                            myRef.child("TYFCB").child(uid).child("received").child(y).child("Referral").setValue(rbref_str);
                            myRef.child("TYFCB").child(uid).child("received").child(y).child("From").setValue(userid);
                            myRef.child("TYFCB").child(uid).child("received").child(y).child("Date").setValue(date_str);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    if(rs.equals("")){
                        inputrs.setError("Fill");
                    }
                }

                startActivity(new Intent(TYFCB_Activity.this, MainActivity.class));
            }
        });
    }
}
