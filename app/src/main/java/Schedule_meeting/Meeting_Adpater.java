package Schedule_meeting;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Profile_Activities.New_Profile_Activity;

public class Meeting_Adpater extends RecyclerView.Adapter<Meeting_Adpater.MeetingViewHolder>{
    private Context context;
    private List<Meeting> results;
    private EditText ed_search;
    private String fname,lname,email,dob,gender,phonenum,fullnamme;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Meeting_Adpater(Context mCtx, List<Meeting> results) {
        context = mCtx;
        this.results = results;
    }


    @NonNull
    @Override
    public Meeting_Adpater.MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_meeting, parent, false);

        return new Meeting_Adpater.MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Meeting_Adpater.MeetingViewHolder holder, final int position) {
        final Meeting result = results.get(position);

        //holder.textViewName.setText(result.uid);
//        String str_status=result.uid;
/*
        String status_meet=str_status.substring(0,1);
        String date_meet=str_status.substring(1);
*/
/*
        if(status_meet.equals("R"))
            holder.textViewEmail.setText(status_meet+"equested");
        else if (status_meet.equals("W"))
            holder.textViewEmail.setText(status_meet+"aiting");
        else
            holder.textViewEmail.setText(status_meet+"ccepted");
        holder.textViewdate.setText(date_meet);
*/


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String userid=result.uid;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status_meet,date_meet;
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                status_meet=dataSnapshot.child("Meeting").child(userid).child(uid).child("status").getValue().toString();
                date_meet=dataSnapshot.child("Meeting").child(userid).child(uid).child("date").getValue().toString();
                fullnamme=fname+" "+lname;
                if(status_meet.equals("R"))
                    holder.textViewEmail.setText(status_meet+"equested");
                else if (status_meet.equals("W"))
                    holder.textViewEmail.setText(status_meet+"aiting");
                else
                    holder.textViewEmail.setText(status_meet+"ccepted");
                holder.textViewdate.setText(date_meet);
                holder.textViewName.setText(fullnamme);
                //holder.textViewEmail.setText(status_meet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, New_Profile_Activity.class);
                intent.putExtra("Uid",userid);

/*
                intent.putExtra("Fullname",fullnamme);
                intent.putExtra("Email",email);
                intent.putExtra("DOB",dob);
                intent.putExtra("Phonenumber",phonenum);
                intent.putExtra("Gender",gender);
                intent.putExtra("Uid",userid);
                intent.putExtra("Status",result.status);
*/
                intent.putExtra("Activity","Meeting");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class MeetingViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,textViewEmail,textViewdate;
        LinearLayout parentlayout;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewEmail = itemView.findViewById(R.id.textview_status);
            textViewdate = itemView.findViewById(R.id.text_view_date);
        }
    }

}

