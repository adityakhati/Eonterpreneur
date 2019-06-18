package TYFCBandReferral_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddSearch_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddSearchAdapter adapter;
    //    private List<Search> artistList;
    private List<AddSearch> results;
    private EditText ed_search;
    private String query_str,activity,groupname;
    FirebaseAuth auth;

    Button btn_grp,btn_con;

    String uid,sending_uid;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizonatal_menu_fragment);
        sending_uid="";

        Intent i=getIntent();
        String activity=i.getExtras().getString("Activity");
        if(activity.equals("refof")) {
            sending_uid = i.getExtras().getString("UID");
            Log.d("Send uid",sending_uid);
        }
        groupname="";

        btn_grp=(Button)findViewById(R.id.btn_grp);
        btn_con=(Button)findViewById(R.id.btn_con);

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = auth.getCurrentUser();
        uid = user.getUid();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(uid).hasChild("Groupname")) {
                    groupname = dataSnapshot.child("Users").child(uid).child("Groupname").getValue().toString();
                }
                else{
                    groupname="";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
        results = new ArrayList<AddSearch>();

//        adapter = new AddSearchAdapter(this, artistList);
        adapter = new AddSearchAdapter(this, results,activity,groupname,sending_uid);
        recyclerView.setAdapter(adapter);

        Query query6 = FirebaseDatabase.getInstance().getReference("Connection/"+uid);
        query6.addValueEventListener(valueEventListener);

        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data("c");
            }
        });

        btn_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupname.equals("")){
                    Toast.makeText(AddSearch_Activity.this,"You Are Not in Any Group",Toast.LENGTH_SHORT).show();
                }
                else
                data("g");
            }
        });

/*
        ed_search = (EditText) findViewById(R.id.input_search);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    query_str = ed_search.getText().toString();
                    Query query6 = FirebaseDatabase.getInstance().getReference("Groups"+"/"+groupname+"/Users")
                            .orderByChild("FirstName")
                            .startAt(query_str)
                            .endAt(query_str + "\uf8ff");
                    query6.addListenerForSingleValueEvent(valueEventListener);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private void data(String select){

        if(select.equals("c")){
            Query query6 = FirebaseDatabase.getInstance().getReference("Connection/"+uid);
            query6.addValueEventListener(valueEventListener);
        }
        else{
            Query query6 = FirebaseDatabase.getInstance().getReference("Groups"+"/"+groupname+"/Users");
            query6.addValueEventListener(valueEventListener);
        }

    }




    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //List<ParentObject> parentObject = new ArrayList<>();
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {
                        results.add(new AddSearch(
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
