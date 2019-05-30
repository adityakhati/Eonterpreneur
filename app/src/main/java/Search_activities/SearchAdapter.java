package Search_activities;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Group_Activities.Group_profile_Activity;
import Profile_Activities.New_Profile_Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ArtistViewHolder> {

    private Context context;
    private List<SearchResult> results;
    private EditText ed_search;
    private String fname,lname,email,dob,gender,phonenum,fullnamme,status,activity,grpname;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public SearchAdapter(Context mCtx, List<SearchResult> results,String activity,String grpname) {
        context = mCtx;
        this.results = results;
        this.activity = activity;
        this.grpname=grpname;
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.layout_parent_search_item, parent, false);

        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistViewHolder holder, final int position) {
        final SearchResult result = results.get(position);
        //holder.textViewName.setText(result.product);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String userid1 = result.product;
        final String name= result.product;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(activity.equals("User")) {
                    String userid=result.product;
                    fname = dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                    lname = dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                    email = dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                    /*gender = dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                    dob = dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                    phonenum = dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
                    *//*if (dataSnapshot.child("Connection").child(uid).hasChild(userid))//childe(userid))
                    {
                        status = dataSnapshot.child("Connection").child(uid).child(userid).getValue().toString();
                        Log.d("STATUS SEARCH ADAPTEE", status);
                    }*/
                    fullnamme = fname + " " + lname;
                    holder.textViewName.setText(fullnamme);
                    holder.textViewEmail.setText(email);
                }
                else if(activity.equals("Incoming req")) {
                    String userid=result.product;
                    fname = dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                    lname = dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                    //email = dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                    status=dataSnapshot.child("Groups").child(grpname).child("Incoming Request").child(userid).getValue().toString();
                    fullnamme = fname + " " + lname;
                    holder.textViewName.setText(fullnamme);
                    holder.textViewEmail.setText(status);
                }
                else if(activity.equals("Group")){
                    String name=result.product;
                    String des=dataSnapshot.child("Groups").child(name).child("description").getValue().toString();
                    holder.textViewName.setText(name);
                    holder.textViewEmail.setText(des);
                }
                else if(activity.equals("GroupRequest")){
                    String name=result.product;
                    String des=dataSnapshot.child("Groups").child(name).child("description").getValue().toString();
                    holder.textViewName.setText(name);
                    holder.textViewEmail.setText(des);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.equals("User")) {
                    Intent intent = new Intent(context, New_Profile_Activity.class);
                    intent.putExtra("Uid", userid1);
                    intent.putExtra("Activity", "Search");
                    context.startActivity(intent);
                }
                else if(activity.equals("Group")){
                    Intent intent = new Intent(context, Group_profile_Activity.class);
                    intent.putExtra("Name",userid1);
                    intent.putExtra("Activity", "joingrp");
                    context.startActivity(intent);
                }
                else if(activity.equals("Incoming req")) {
                    Intent intent = new Intent(context, New_Profile_Activity.class);
                    intent.putExtra("Uid", userid1);
                    intent.putExtra("Groupname",grpname);
                    intent.putExtra("Activity", "Incomingreq");
                    context.startActivity(intent);
                }
                else if(activity.equals("GroupRequest")){
                    Intent intent = new Intent(context, Group_profile_Activity.class);
                    intent.putExtra("Name",userid1);
                    intent.putExtra("Activity", "reqgrp");
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,textViewEmail;
        LinearLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewEmail = itemView.findViewById(R.id.textview_email);

        }
    }
}