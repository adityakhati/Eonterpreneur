package Group_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Create_group_Activity extends AppCompatActivity {

    EditText grp_name,grp_des;
    Button btn_create;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        grp_des=(EditText)findViewById(R.id.input_des);
        grp_name=(EditText)findViewById(R.id.input_grp_name);
        btn_create=(Button)findViewById(R.id.btn_create_grp);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_des=grp_des.getText().toString();
                final String str_name=grp_name.getText().toString();

                if(str_des==null)
                    grp_des.setError("Fill Group Description");
                if(str_name==null)
                    grp_name.setError("Fill Group Name");

                mAuth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userID = user.getUid();

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Users").child(userID).child("Groupname").hasChild(str_name)){
                            Toast.makeText(getApplicationContext(),"Group Name exists use another",Toast.LENGTH_SHORT).show();

                        }
                    else {
                            myRef.child("Users").child(userID).child("Groupname").setValue(str_name);
                            myRef.child("Groups").child(str_name).child("description").setValue(str_des);
                            myRef.child("Groups").child(str_name).child("Users").child(userID).setValue("admin");

                            startActivity(new Intent(Create_group_Activity.this, MainActivity.class));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }
}
