package com.example.android.evineon;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Group_Activities.Group_Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Groups_Meeting_show_Activity extends AppCompatActivity {


    String groupname,usertype;

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;
    FirebaseAuth auth;
    private List<GroupMeeting> results;


    private RecyclerView recyclerView;
    private Group_Meeting_adapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Groups_Meeting_show_Activity.this, Group_Activity.class);
        intent.putExtra("Groupname",groupname);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_);

        Intent intent=getIntent();
        groupname=intent.getExtras().getString("Groupname");
        usertype=intent.getExtras().getString("Usertype");

/*        if(usertype.equals("treasurer")) {
        }
        else {
            findViewById(R.id.floating_action_add_acc).setVisibility(View.GONE);
        }

        findViewById(R.id.floating_action_add_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Groups_Meeting_show_Activity.this,Add_Account_Activity.class);
                intent.putExtra("Groupname",groupname);
                startActivity(intent);

            }
        });*/


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<GroupMeeting>();

        adapter = new Group_Meeting_adapter(this, results,groupname);
        recyclerView.setAdapter(adapter);

/*
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //final String uid = user.getUid();
*/


        Query query6 = FirebaseDatabase.getInstance().getReference("Groups/"+groupname+"/Meeting");
        query6.addListenerForSingleValueEvent(valueEventListener);


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //List<ParentObject> parentObject = new ArrayList<>();
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {
                        results.add(new GroupMeeting(
                                snapshot.getKey()
                        ));
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
