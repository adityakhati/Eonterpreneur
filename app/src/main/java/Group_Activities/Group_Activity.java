package Group_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.evineon.Group_meeting_Activity;
import com.example.android.evineon.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Account_Activities.Accounts_Activity;
import Chat_Activities.Chatting_Activity;
import Search_activities.SearchActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Group_Activity extends AppCompatActivity {
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;


    String usertype,groupname;
    private String uid;
    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takedata();
    }

    private void group(){

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.floating_action_add_participants);

        if (usertype.equals("superadmin") || usertype.equals("admin")) {
        }
        else{
            materialDesignFAM.setVisibility(View.GONE);
        }

        findViewById(R.id.card_view_chat_room).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent intent=new Intent(Group_Activity.this, Chatting_Activity.class);
                intent.putExtra("Groupname",groupname);
                startActivity(intent);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                Intent intent=new Intent(Group_Activity.this, SearchActivity.class);
                intent.putExtra("Search","Incoming req");
                intent.putExtra("Groupname",groupname);
                startActivity(intent);    }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                startActivity(new Intent(Group_Activity.this, Add_partcipants_Activity.class));

            }
        });

        findViewById(R.id.card_view_accounts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, Accounts_Activity.class);
                intent.putExtra("Groupname",groupname);
                intent.putExtra("Usertype",usertype);
                startActivity(intent);
            }
        });

        findViewById(R.id.card_view_group_heirarchy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, User_Group_Activity.class);
                intent.putExtra("Groupname",groupname);
                intent.putExtra("Usertype",usertype);
                startActivity(intent);
            }
        });

        findViewById(R.id.card_view_meeting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, Group_meeting_Activity.class);
                intent.putExtra("Groupname",groupname);
                intent.putExtra("Usertype",usertype);
                startActivity(intent);
            }
        });
    }

    private void takedata() {
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = auth.getCurrentUser();
        uid = user.getUid();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usertype = dataSnapshot.child("Users").child(uid).child("usertype").getValue().toString();
                if(dataSnapshot.child("Users").child(uid).hasChild("Groupname")) {
                    groupname = dataSnapshot.child("Users").child(uid).child("Groupname").getValue().toString();
                    Toast.makeText(getApplicationContext(),usertype, Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_group);
                    group();

                }
                else{

                    if (usertype.equals("superadmin") || usertype.equals("admin")) {
                        setContentView(R.layout.activity_admin_groups_dashboard);
                        adminuser();
                    } else {
                        setContentView(R.layout.activity_other_groups_dashboard);
                        otheruser();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void adminuser(){
        findViewById(R.id.card_view_join_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, SearchActivity.class);
                intent.putExtra("Search","Group");
                startActivity(intent);
            }
        });

        findViewById(R.id.card_view_create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Group_Activity.this, Create_group_Activity.class));
            }
        });

        findViewById(R.id.card_view_requested_groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, SearchActivity.class);
                intent.putExtra("Search","GroupRequest");
                startActivity(intent);
            }
        });
    }

    private void otheruser(){
        findViewById(R.id.card_view_join_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, SearchActivity.class);
                intent.putExtra("Search","Group");
                startActivity(intent);
            }
        });

        findViewById(R.id.card_view_requested_groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_Activity.this, SearchActivity.class);
                intent.putExtra("Search","GroupRequest");
                startActivity(intent);

            }
        });
    }
}