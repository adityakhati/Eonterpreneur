package Group_Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.android.evineon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Search_activities.SearchResult;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Add_partcipants_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Add_participant_adapter adapter;
    //    private List<Search> artistList;
    private List<SearchResult> results;
    private EditText ed_search;
    private String query_str,groupname;

    private static final String TAG = "SearchActivity";

    DatabaseReference db;
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partcipants);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<SearchResult>();

//        adapter = new SearchAdapter(this, artistList);
        adapter = new Add_participant_adapter(this, results);
        recyclerView.setAdapter(adapter);

        //query_str = ed_search.getText().toString();
        Query query6 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("FirstName");
        query6.addListenerForSingleValueEvent(valueEventListener);




        ed_search = (EditText) findViewById(R.id.input_search);

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query_str = ed_search.getText().toString();
                Query query6 = FirebaseDatabase.getInstance().getReference("Users")
                        .orderByChild("FirstName")
                        .startAt(query_str)
                        .endAt(query_str+"\uf8ff");

                query6.addListenerForSingleValueEvent(valueEventListener);

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

                        if(!snapshot.hasChild("Groupname")) {

                            results.add(new SearchResult(
                                    snapshot.getKey()
                            ));
                        }

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
