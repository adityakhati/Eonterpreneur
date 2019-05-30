package Group_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Search_User_Group_Activity extends AppCompatActivity {

    String name,fname,lname,fullname;
    DatabaseReference myRef,ref;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;
    TextView tvname,tvadmin,tvdes;


    String[] list1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__user__group_);
        Intent i=getIntent();
        name=i.getExtras().getString("Groupname");

        ArrayList<String> ar = new ArrayList<String>();



/*
        String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
                "WebOS","Ubuntu","Windows7","Max OS X"};
*/

     /*   ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_view_user_group, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
*/
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_view_user_group, list1);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        myRef.child("Groups").child(name).child("Users").orderByValue().equalTo("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            int i=0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String userid="";
                    String value = snap.getValue(String.class);
                    userid = snap.getKey();
                    final String uid=userid;
                    if(!userid.equals("")) {
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        ref = mFirebaseDatabase.getReference();
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                fname = dataSnapshot.child("Users").child(uid).child("FirstName").getValue().toString();
                                lname = dataSnapshot.child("Users").child(uid).child("LastName").getValue().toString();
                                fullname = fname +" "+ lname;
                                Log.d("Fullname",fullname);
                                //tvadmin.setText(fullname);
                                //list1[i]=fullname;
                                //Log.d("List",list1[0]);
                                //i++;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                add();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void add(){
//        Log.d("List",list1[0]);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_view_user_group, list1);
        //ListView listView = (ListView) findViewById(R.id.mobile_list);
        //listView.setAdapter(adapter);
    }
}

