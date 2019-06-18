package Account_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import Group_Activities.Group_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Accounts_Activity extends AppCompatActivity {


    String groupname,usertype;

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;
    FirebaseAuth auth;
    private List<Account> results;


    private RecyclerView recyclerView;
    private Accounts_adapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(Accounts_Activity.this, Group_Activity.class);
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

        if(usertype.equals("treasurer")) {
        }
        else {
            findViewById(R.id.floating_action_add_acc).setVisibility(View.GONE);
        }

        findViewById(R.id.floating_action_add_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Accounts_Activity.this,Add_Account_Activity.class);
                intent.putExtra("Groupname",groupname);
                startActivity(intent);

            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
//        artistList = new ArrayList<>();
        results = new ArrayList<Account>();

//        adapter = new Account_Adpater(this, artistList);
        adapter = new Accounts_adapter(this, results,groupname);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();


        //query_str = ed_search.getText().toString();
        Query query6 = FirebaseDatabase.getInstance().getReference("Groups/"+groupname+"/Accounts");
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
                        results.add(new Account(
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
