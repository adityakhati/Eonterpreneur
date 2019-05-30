package Chat_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.evineon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Chatting_Activity extends AppCompatActivity {

    EditText inp_msg;
    String msg_str,userid,groupname;
    FloatingActionButton fb1;

    private RecyclerView recyclerView;
    private Chat_adapter adapter;

    DatabaseReference myRef,ref;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;

    DatabaseReference dbArtists;
    private DataSnapshot dataSnapshot;
    private List<Chat> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent=getIntent();
        groupname=intent.getExtras().getString("Groupname");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        inp_msg=(EditText)findViewById(R.id.input_message);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbArtists = FirebaseDatabase.getInstance().getReference();
        results = new ArrayList<Chat>();

        //adapter = new Chat_adapter(this, results,groupname);
        adapter = new Chat_adapter(this,results,groupname);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();


        //query_str = ed_search.getText().toString();
        Query query6 = FirebaseDatabase.getInstance().getReference("Groups/"+groupname+"/Message");
        query6.addValueEventListener(valueEventListener);



        findViewById(R.id.fab_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_str=inp_msg.getText().toString();
                auth=FirebaseAuth.getInstance();
                FirebaseUser u=auth.getCurrentUser();
                userid=u.getUid();

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String x="1";
                        int i;
                        for(i=1;i<10;i++) {
                            x=Integer.toString(i);
                            Log.d("Value",x);
                            if(!dataSnapshot.child("Groups").child(groupname).child("Message").hasChild(x)) {
                                break;
                            }
                        }

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        DateFormat datF = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        //System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
                        String dt=dateFormat.format(date);
                        String time=datF.format(date);
                        myRef.child("Groups").child(groupname).child("Message").child(x).child("By").setValue(userid);
                        myRef.child("Groups").child(groupname).child("Message").child(x).child("text").setValue(msg_str);
                        myRef.child("Groups").child(groupname).child("Message").child(x).child("time").setValue(time);
                        myRef.child("Groups").child(groupname).child("Message").child(x).child("date").setValue(dt);
                        inp_msg.setText("");

                        Query query6 = FirebaseDatabase.getInstance().getReference("Groups/"+groupname+"/Message");
                        query6.addValueEventListener(valueEventListener);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
                        results.add(new Chat(
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

