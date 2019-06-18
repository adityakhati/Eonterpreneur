package TYFCBandReferral_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Show_Activity extends AppCompatActivity {


    String type,act;

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;
    FirebaseAuth auth;
    private List<Show> results;


    private RecyclerView recyclerView;
    private ShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent i=getIntent();
        type=i.getExtras().getString("type");
        act=i.getExtras().getString("act");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
        results = new ArrayList<Show>();

        adapter = new ShowAdapter(this, results,type,act);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();

        if(act.equals("ref")) {
            if (type.equals("rec")) {
                Query query6 = FirebaseDatabase.getInstance().getReference("Referral/" + uid + "/received");
                query6.addListenerForSingleValueEvent(valueEventListener);
            } else {
                Query query6 = FirebaseDatabase.getInstance().getReference("Referral/" + uid + "/given");
                query6.addListenerForSingleValueEvent(valueEventListener);
            }
        }
        else{
            if (type.equals("rec")) {
                Query query6 = FirebaseDatabase.getInstance().getReference("TYFCB/" + uid + "/received");
                query6.addListenerForSingleValueEvent(valueEventListener);
            } else {
                Query query6 = FirebaseDatabase.getInstance().getReference("TYFCB/" + uid + "/given");
                query6.addListenerForSingleValueEvent(valueEventListener);
            }
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
                        results.add(new Show(
                                snapshot.getKey()

                        ));
                        Log.d("Number",snapshot.getKey());
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
