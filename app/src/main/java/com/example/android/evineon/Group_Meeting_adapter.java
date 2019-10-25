package com.example.android.evineon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Group_Meeting_adapter extends RecyclerView.Adapter<Group_Meeting_adapter.ArtistViewHolder> {

    private Context context;
    private List<GroupMeeting> results;
    private String fname,lname,date,fullnamme;
    private String userid,groupname;


    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Group_Meeting_adapter(Context mCtx, List<GroupMeeting> results, String groupname) {
        context = mCtx;
        this.results = results;
        this.groupname=groupname;
    }


    @NonNull
    @Override
    public Group_Meeting_adapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_show, parent, false);
        return new Group_Meeting_adapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Group_Meeting_adapter.ArtistViewHolder holder, final int position) {
        final GroupMeeting result = results.get(position);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String num=result.num;
        Log.d("Num",num);
        Log.d("Groupname",groupname);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userid=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("by").getValue().toString();
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                date=dataSnapshot.child("Groups").child(groupname).child("Meeting").child(num).child("date").getValue().toString();
                fullnamme=fname+" "+lname;
                holder.textViewName.setText(fullnamme);
                holder.textViewdate.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Group_meeting_details_Activity.class);
                intent.putExtra("Groupname",groupname);
                intent.putExtra("Num",num);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,textViewdate;
        LinearLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewdate = itemView.findViewById(R.id.text_view_date);

        }
    } 
}
