package Profile_Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import androidx.appcompat.app.AppCompatActivity;

public class Meeting_Profile_activity extends AppCompatActivity {

    ImageView img_profile_pic;
    Button connect, request_bt, accept_bt, reject_bt, meet_bt;
    TextView tvname, tvemail, tvgender, tvdob, tvphonenuber, tvmeet;
    private String email, dob, gender, phonenum, fullname, fname, lname, uid, userid, status,status_meet,date_meet, activity, url,time_meet;


    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private void takedate() {
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        uid = user.getUid();


        Intent intent = getIntent();
        userid = intent.getStringExtra("Uid");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname = dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname = dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                email = dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                gender = dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                dob = dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                phonenum = dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
                if (dataSnapshot.child("Connection").child(uid).hasChild(userid))//childe(userid))
                {
                    status = dataSnapshot.child("Connection").child(uid).child(userid).getValue().toString();
                    Log.d("STATUS SEARCH ADAPTER", status);
                }
                fullname = fname + " " + lname;
                tvdob.setText(dob);
                tvemail.setText(email);
                tvname.setText(fullname);
                tvgender.setText(gender);
                tvphonenuber.setText(phonenum);
                if (status == null)
                    status = "";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void ShowImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference ref = storage.getReference();

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Intent intent = getIntent();
        userid = intent.getStringExtra("Uid");


        String imgref = "Users/" + userid + "/Profile_pic";
        userid = intent.getStringExtra("Uid");


        ref.child(imgref).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                url = uri.toString();
                Picasso.get()
                        .load(url)
                        .into(img_profile_pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__profile_activity);


    }

    @Override
    protected void onStart() {
        super.onStart();
        ShowImage();
    }
}
