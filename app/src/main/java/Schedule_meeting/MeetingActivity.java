package Schedule_meeting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import Search_activities.SearchActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MeetingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Meeting_Adpater adapter;
    //    private List<Search> artistList;
    private List<Meeting> results;
    private EditText ed_search;
    private String query_str;

    private static final String TAG = "SearchActivity";

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        recyclerView = findViewById(R.id.reclervieworders_connection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<Meeting>();

//        adapter = new Meeting_Adpater(this, artistList);
        adapter = new Meeting_Adpater(this, results);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();


        //query_str = ed_search.getText().toString();
        Query query6 = FirebaseDatabase.getInstance().getReference("Meeting/"+uid);
        query6.addListenerForSingleValueEvent(valueEventListener);



        //FloatingActionButton floatingActionButton1 = (FloatingActionButton) ;

        findViewById(R.id.material_design_android_floating_action_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeetingActivity.this, SearchActivity.class);
                intent.putExtra("Search","User");
                startActivity(intent);

            }
        });


/*
                //TODO something when floating action menu first item clicked
                Intent intent=new Intent(MeetingActivity.this, SearchActivity.class);
                intent.putExtra("Search","User");
                startActivity(intent);
*/


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //List<ParentObject> parentObject = new ArrayList<>();
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {
                        results.add(new Meeting(
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
