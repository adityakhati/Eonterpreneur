package Group_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.android.evineon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class User_Group_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Group_User_adapter adapter;
    //    private List<Search> artistList;
    private List<Group_Users> results;
    private EditText ed_search;
    private String query_str,activity,groupname;

    DatabaseReference myRef,ref;
    FirebaseDatabase mFirebaseDatabase;

    String fname,lname,email,fullname;


    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        Intent i=getIntent();
        groupname=i.getExtras().getString("Groupname");


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<Group_Users>();

//        adapter = new SearchAdapter(this, artistList);
        adapter = new Group_User_adapter(this, results, activity);
        recyclerView.setAdapter(adapter);

        //query_str = ed_search.getText().toString();

    mFirebaseDatabase = FirebaseDatabase.getInstance();
    myRef = mFirebaseDatabase.getReference();

        myRef.child("Groups").child(groupname).child("Users").orderByValue().equalTo("admin").addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot){
            results.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                String userid = "";
                String value = snap.getValue(String.class);
                userid = snap.getKey();
                final String uid = userid;
                if (!userid.equals("")) {
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    ref = mFirebaseDatabase.getReference();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                            lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                            email = dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();
                            fullname = fname + " " + lname;
                            Log.d("Fullname", fullname);
                                results.add(new Group_Users(
                                        uid,fullname,email));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }


         }
        );
    }
}
