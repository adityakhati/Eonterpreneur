package Search_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
//    private List<Search> artistList;
    private List<SearchResult> results;
    private EditText ed_search;
    private String query_str,activity,groupname;
    FirebaseAuth auth;

    String userid;


    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        groupname="";

        Intent i=getIntent();
        activity=i.getExtras().getString("Search");
        if(activity.equals("Incoming req")){
            groupname=i.getExtras().getString("Groupname");
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<SearchResult>();

//        adapter = new SearchAdapter(this, artistList);
        adapter = new SearchAdapter(this, results,activity,groupname);
        recyclerView.setAdapter(adapter);

        //query_str = ed_search.getText().toString();
        if(activity.equals("User")) {
            Query query6 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("FirstName");
            query6.addListenerForSingleValueEvent(valueEventListener);
        }
        else if(activity.equals("Group")){
            Query query6 = FirebaseDatabase.getInstance().getReference("Groups").orderByKey();
            query6.addListenerForSingleValueEvent(valueEventListener);

        }
        else if(activity.equals("Incoming req")){
            Query query6 = FirebaseDatabase.getInstance().getReference("Groups/"+groupname+"/Incoming Request").orderByKey();
            query6.addListenerForSingleValueEvent(valueEventListener);
        }
        else if(activity.equals("GroupRequest")){
            auth=FirebaseAuth.getInstance();
            FirebaseUser u=auth.getCurrentUser();
            userid=u.getUid();
            Query query6 = FirebaseDatabase.getInstance().getReference("Users"+"/"+userid+"/Group Requested").orderByKey();
            query6.addListenerForSingleValueEvent(valueEventListener);
        }
            //1. SELECT * FROM Artists
        //dbArtists = FirebaseDatabase.getInstance().getReference();
            /*
            //2. SELECT * FROM Artists WHERE id = "-LAJ7xKNj4UdBjaYr8Ju"
            Query query = FirebaseDatabase.getInstance().getReference()
                    .orderByChild("id")
                    .equalTo("4aCUdJQmfiZjGInpI3arcSgjJDv2");
            //query.addListenerForSingleValueEvent(valueEventListener);

            //3. SELECT * FROM Artists WHERE country = "India"
            Query query3 = FirebaseDatabase.getInstance().getReference()
                    .orderByChild("country")
                    .equalTo("India");
            //query3.addListenerForSingleValueEvent(valueEventListener);

            //4. SELECT * FROM Artists LIMIT 2
            Query query4 = FirebaseDatabase.getInstance().getReference("Artists").limitToFirst(2);


            //5. SELECT * FROM Artists WHERE age < 30
            Query query5 = FirebaseDatabase.getInstance().getReference()
                    .orderByChild("age")
                    .endAt(29);
            query5.addListenerForSingleValueEvent(valueEventListener);
            */

//        Button btn = (Button) findViewById(R.id.btn_click);
        ed_search = (EditText) findViewById(R.id.input_search);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(activity.equals("User")) {
                    query_str = ed_search.getText().toString();
                    Query query6 = FirebaseDatabase.getInstance().getReference("Users")
                            .orderByChild("FirstName")
                            .startAt(query_str)
                            .endAt(query_str + "\uf8ff");
                    query6.addListenerForSingleValueEvent(valueEventListener);
                }
                else if(activity.equals("Incoming req")){

                    Query query6 = FirebaseDatabase.getInstance().getReference("Groups/"+groupname+"/Incoming Request")
                            .orderByKey()
                            .startAt(query_str)
                            .endAt(query_str + "\uf8ff");

                    query6.addListenerForSingleValueEvent(valueEventListener);
                }
                else if(activity.equals("Group")){
                    query_str = ed_search.getText().toString();
                    Query query6 = FirebaseDatabase.getInstance().getReference("Groups")
                            .orderByKey()
                            .startAt(query_str)
                            .endAt(query_str + "\uf8ff");
                    query6.addListenerForSingleValueEvent(valueEventListener);
                }
/*                else if(activity.equals("GUser")){
                    Query query6 = FirebaseDatabase.getInstance().getReference("Groups").child("Users")
                            .orderByKey()
                            .startAt(query_str)
                            .endAt(query_str + "\uf8ff");
                    query6.addListenerForSingleValueEvent(valueEventListener);
                }*/
                else if(activity.equals("GroupRequest")){
                    query_str = ed_search.getText().toString();
                    Query query6 = FirebaseDatabase.getInstance().getReference("Users"+"/"+userid+"/Group Requested")
                            .orderByKey()
                            .startAt(query_str)
                            .endAt(query_str + "\uf8ff");
                    query6.addListenerForSingleValueEvent(valueEventListener);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //List<ParentObject> parentObject = new ArrayList<>();
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {
                        results.add(new SearchResult(
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
