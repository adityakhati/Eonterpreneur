package Connection;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Profile_Activities.New_Profile_Activity;


public class Connection_Adapter extends RecyclerView.Adapter<Connection_Adapter.ConnectionViewHolder>{
    private Context context;
    private List<Connection> results;
    private EditText ed_search;
    private String fname,lname,email,dob,gender,phonenum,fullnamme;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Connection_Adapter(Context mCtx, List<Connection> results) {
        context = mCtx;
        this.results = results;
    }


    @NonNull
    @Override
    public Connection_Adapter.ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpater_connection, parent, false);

        return new Connection_Adapter.ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Connection_Adapter.ConnectionViewHolder holder, final int position) {
        final Connection result = results.get(position);

        //holder.textViewName.setText(result.uid);
        holder.textViewEmail.setText(result.status);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String userid=result.uid;
        /*auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        */
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                email=dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                gender=dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                dob=dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                phonenum=dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
                fullnamme=fname+" "+lname;
                holder.textViewName.setText(fullnamme);
                //holder.textViewEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, New_Profile_Activity.class);
                intent.putExtra("Fullname",fullnamme);
                intent.putExtra("Email",email);
                intent.putExtra("DOB",dob);
                intent.putExtra("Phonenumber",phonenum);
                intent.putExtra("Gender",gender);
                intent.putExtra("Uid",userid);
                intent.putExtra("Status",result.status);
                intent.putExtra("Activity","Connection");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ConnectionViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,textViewEmail;
        LinearLayout parentlayout;

        public ConnectionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewEmail = itemView.findViewById(R.id.textview_status);

        }
    }

}
