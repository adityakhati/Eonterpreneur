package TYFCBandReferral_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Referral_Activity extends AppCompatActivity {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private EditText inputname,inputadd,inputcomment,inputemail,inputphone;
    Button btnconfirm;
    Button btn_grp,btn_refof;
    String uid,name,userid,send_uid;
    
    private RadioGroup rgref,rgbusi;
    private RadioButton rbref,rbbusi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_);
        uid="";
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        FirebaseUser u_id=auth.getCurrentUser();
        userid=u_id.getUid();

        btn_grp = (Button) findViewById(R.id.btn_add_grp);
        btnconfirm = (Button) findViewById(R.id.btn_confirm);
        btn_refof = (Button) findViewById(R.id.btn_add_part);

        inputadd = (EditText) findViewById(R.id.input_address);
        inputemail=(EditText) findViewById(R.id.input_email);
        inputname=(EditText) findViewById(R.id.input_name);
        inputcomment=(EditText) findViewById(R.id.input_comment);
        inputphone=(EditText) findViewById(R.id.input_num);
        
        rgref = (RadioGroup) findViewById(R.id.rad_reff);


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent=new Intent(Referral_Activity.this, Show_Activity.class);
                intent.putExtra("type","given");
                intent.putExtra("act","ref");
                startActivity(intent);

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent=new Intent(Referral_Activity.this, Show_Activity.class);
                intent.putExtra("type","rec");
                intent.putExtra("act","ref");
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
        else if(act.equals("addref"))
        {
            uid=i.getExtras().getString("Uid");
            send_uid=i.getExtras().getString("Sending");
            Log.d("UID ",send_uid);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String fname1=dataSnapshot.child("Users").child(send_uid).child("FirstName").getValue().toString();
                    String lname1=dataSnapshot.child("Users").child(send_uid).child("LastName").getValue().toString();
                    String fullname1=fname1+" "+lname1;
                    btn_grp.setText(fullname1);

                    String fname=dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                    String lname=dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                    String email=dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();
                    String loc=dataSnapshot.child("Users").child(uid).child("City").getValue().toString();
                    String phonenum=dataSnapshot.child("Users").child(uid).child("PhoneNumber").getValue().toString();
                    String fullname=fname+" "+lname;
                    inputphone.setText(phonenum);
                    inputadd.setText(loc);
                    inputemail.setText(email);
                    inputname.setText(fullname);
                    uid=send_uid;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        btn_refof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.equals("")){
                    btn_grp.setError("First Fill This Field");
                }
                else {
                    Intent intent = new Intent(Referral_Activity.this, AddSearch_Activity.class);
                    intent.putExtra("Activity","refof");
                    intent.putExtra("UID",uid);
                    startActivity(intent);
                    }
            }
        });

        btn_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Referral_Activity.this, AddSearch_Activity.class);
                intent.putExtra("Activity","ref");
                startActivity(intent);
            }
        });


        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address=inputadd.getText().toString();
                final String name=inputname.getText().toString();
                final String email=inputemail.getText().toString();
                final String comment=inputcomment.getText().toString();
                final String phonenum=inputphone.getText().toString();
                String thank_grp=uid;


                final int rdref = rgref.getCheckedRadioButtonId();
                rbref = (RadioButton) findViewById(rdref);
                final String rbref_str = rbref.getText().toString();



                if(!name.equals("") && !email.equals("") && !address.equals("") && !phonenum.equals("") && !thank_grp.equals("")){

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String x,y;
                            int i;
                            for (i = 1; ; i++) {
                                x = Integer.toString(i);
                                if (!dataSnapshot.child("Referral").child(userid).child("given").hasChild(x)) {
                                    break;
                                }
                            }
                            for (i = 1; ; i++) {
                                y = Integer.toString(i);
                                if (!dataSnapshot.child("Referral").child(uid).child("received").hasChild(y)) {
                                    break;
                                }
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = new Date();
                            String date_str=formatter.format(date);

                            myRef.child("Referral").child(userid).child("given").child(x).child("Name").setValue(name);
                            myRef.child("Referral").child(userid).child("given").child(x).child("Comment").setValue(comment);
                            myRef.child("Referral").child(userid).child("given").child(x).child("Ref").setValue(rbref_str);
                            myRef.child("Referral").child(userid).child("given").child(x).child("To").setValue(uid);
                            myRef.child("Referral").child(userid).child("given").child(x).child("Address").setValue(address);
                            myRef.child("Referral").child(userid).child("given").child(x).child("PhoneNum").setValue(phonenum);
                            myRef.child("Referral").child(userid).child("given").child(x).child("Email").setValue(email);
                            myRef.child("Referral").child(userid).child("given").child(x).child("Date").setValue(date_str);

                            myRef.child("Referral").child(uid).child("received").child(y).child("Name").setValue(name);
                            myRef.child("Referral").child(uid).child("received").child(y).child("Comment").setValue(comment);
                            myRef.child("Referral").child(uid).child("received").child(y).child("Ref").setValue(rbref_str);
                            myRef.child("Referral").child(uid).child("received").child(y).child("From").setValue(userid);
                            myRef.child("Referral").child(uid).child("received").child(y).child("Address").setValue(address);
                            myRef.child("Referral").child(uid).child("received").child(y).child("PhoneNum").setValue(phonenum);
                            myRef.child("Referral").child(uid).child("received").child(y).child("Email").setValue(email);
                            myRef.child("Referral").child(uid).child("received").child(y).child("Date").setValue(date_str);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    if(name.equals("")){
                        inputname.setError("Fill");
                    }
                    else if(address.equals(""))
                        inputadd.setError("Fill");
                    else if(phonenum.equals(""))
                        inputphone.setError("Fill");
                    else if(email.equals(""))
                        inputemail.setError("Fill");
                }

                startActivity(new Intent(Referral_Activity.this, MainActivity.class));
            }
        });

    }
}
