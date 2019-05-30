package Group_Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

import Search_activities.SearchResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Add_participant_adapter extends RecyclerView.Adapter<Add_participant_adapter.ArtistViewHolder>{


    private Context context;
    private List<SearchResult> results;
    private EditText ed_search;
    private String fname,lname,email,dob,gender,phonenum,fullnamme,status,groupname;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Add_participant_adapter(Context mCtx, List<SearchResult> results) {
        context = mCtx;
        this.results = results;
    }


    @NonNull
    @Override
    public Add_participant_adapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_add_participants, parent, false);

        return new Add_participant_adapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Add_participant_adapter.ArtistViewHolder holder, final int position) {
        final SearchResult result = results.get(position);
        //holder.textViewName.setText(result.product);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String userid=result.product;
/*
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();
*/

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                if(dataSnapshot.child("Users").child(userid).hasChild("Groupname"))
                groupname=dataSnapshot.child("Users").child(userid).child("Groupname").getValue().toString();
                email=dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();

            /*  gender=dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                dob=dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                phonenum=dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
            */
                fullnamme = fname + " " + lname;
                holder.textViewName.setText(fullnamme);
                holder.textViewEmail.setText(email);
            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,textViewEmail;
        CheckBox checkBox;
        LinearLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewEmail = itemView.findViewById(R.id.textview_email);
            checkBox = itemView.findViewById(R.id.checkbox_add_part);

        }
    }
}
