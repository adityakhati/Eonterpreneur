package Connection;

import android.os.Bundle;
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


public class Connection_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Connection_Adapter adapter;
    //    private List<Search> artistList;
    private List<Connection> results;
    private EditText ed_search;
    private String query_str;

    private static final String TAG = "SearchActivity";

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_request);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        recyclerView = findViewById(R.id.reclervieworders_connection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<Connection>();

//        adapter = new Connection_Adapter(this, artistList);
        adapter = new Connection_Adapter(this, results);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();


        //query_str = ed_search.getText().toString();
        Query query6 = FirebaseDatabase.getInstance().getReference("Connection/"+uid);
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
                        results.add(new Connection(
                                snapshot.getKey(),
                                snapshot.getValue().toString()
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
