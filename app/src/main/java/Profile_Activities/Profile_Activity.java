package Profile_Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.evineon.R;
import com.example.android.evineon.Upload_image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Profile_Activity extends AppCompatActivity {

    ImageView img_upload_btn,img_profile_pic;
    String url,uid;
    TextView tvname,tvemail,tvgender,tvdob,tvphonenuber,tvuser,tvclass,tvorg;

    FloatingActionButton editprofile;


    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);


        img_upload_btn=(ImageView)findViewById(R.id.camera_asset_upload_btn);
        img_profile_pic=(ImageView)findViewById(R.id.img_profile_pic);
        editprofile=(FloatingActionButton)findViewById(R.id.profile_edit_fb);

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile_Activity.this, Profile_edit_Activity.class));
            }
        });


        /*tvdob=(TextView)findViewById(R.id.dob);
        tvemail=(TextView)findViewById(R.id.email);
        tvname=(TextView)findViewById(R.id.name);
        tvgender=(TextView)findViewById(R.id.gender);
        tvuser=(TextView)findViewById(R.id.usertype);
*/
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        auth = FirebaseAuth.getInstance();
        FirebaseUser userid=auth.getCurrentUser();
        uid=userid.getUid();


        img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Profile_Activity.this, Upload_image.class);
                i.putExtra("Activity","Profile");
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ShowImage();
        loaddata();
    }

    private void loaddata(){

        tvdob=(TextView)findViewById(R.id.dob);
        tvemail=(TextView)findViewById(R.id.email);
        tvname=(TextView)findViewById(R.id.name);
        tvgender=(TextView)findViewById(R.id.gender);
        tvphonenuber=(TextView)findViewById(R.id.phonenumber);
        tvuser=(TextView)findViewById(R.id.usertype);
        tvclass=(TextView)findViewById(R.id.class_tv);
        tvorg=(TextView)findViewById(R.id.org_tv);


        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        final String userID = user.getUid();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String fname=dataSnapshot.child("Users").child(userID).child("FirstName").getValue().toString();
                String lname=dataSnapshot.child("Users").child(userID).child("LastName").getValue().toString();
                String email=dataSnapshot.child("Users").child(userID).child("Email").getValue().toString();
                String gender=dataSnapshot.child("Users").child(userID).child("Gender").getValue().toString();
                String dob=dataSnapshot.child("Users").child(userID).child("DOB").getValue().toString();
                String phonenum=dataSnapshot.child("Users").child(userID).child("PhoneNumber").getValue().toString();
                String usertype=dataSnapshot.child("Users").child(userID).child("usertype").getValue().toString();
                String classstr=dataSnapshot.child("Users").child(userID).child("class").getValue().toString();
                String orgstr=dataSnapshot.child("Users").child(userID).child("org").getValue().toString();
                tvdob.setText(dob);
                tvemail.setText(email);
                fname=fname+" "+lname;
                tvname.setText(fname);
                tvgender.setText(gender);
                tvphonenuber.setText(phonenum);
                tvorg.setText(orgstr);
                tvclass.setText(classstr);
                tvuser.setText(usertype.toUpperCase()+" User");
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ShowImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference ref = storage.getReference();

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        String imgref="Users/"+uid+"/Profile_pic";

        ref.child(imgref).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                url=uri.toString();
                Picasso.get()
                        .load(url)
                        .into(img_profile_pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(Profile_Activity.this,"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
