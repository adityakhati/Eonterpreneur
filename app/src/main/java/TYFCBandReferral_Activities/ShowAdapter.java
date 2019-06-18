package TYFCBandReferral_Activities;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ArtistViewHolder> {

    private Context context;
    private List<Show> results;
    private EditText ed_search;
    private String fname,lname,date,act,type,fullnamme;
    private String userid;


    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public ShowAdapter(Context mCtx, List<Show> results,String type,String act) {
        context = mCtx;
        this.results = results;
        this.type=type;
        this.act=act;
    }


    @NonNull
    @Override
    public ShowAdapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_show, parent, false);
        return new ShowAdapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowAdapter.ArtistViewHolder holder, final int position) {
        final Show result = results.get(position);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String num=result.num;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String uid = user.getUid();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(act.equals("ref")) {
                    if (type.equals("rec")) {
                        userid = dataSnapshot.child("Referral").child(uid).child("received").child(num).child("From").getValue().toString();
                        date = dataSnapshot.child("Referral").child(uid).child("received").child(num).child("Date").getValue().toString();
                    } else {
                        userid = dataSnapshot.child("Referral").child(uid).child("given").child(num).child("To").getValue().toString();
                        date = dataSnapshot.child("Referral").child(uid).child("given").child(num).child("Date").getValue().toString();
                    }
                }
                else{
                    if (type.equals("rec")) {
                        userid = dataSnapshot.child("TYFCB").child(uid).child("received").child(num).child("From").getValue().toString();
                        date = dataSnapshot.child("TYFCB").child(uid).child("received").child(num).child("Date").getValue().toString();
                    } else {
                        userid = dataSnapshot.child("TYFCB").child(uid).child("given").child(num).child("To").getValue().toString();
                        date = dataSnapshot.child("TYFCB").child(uid).child("given").child(num).child("Date").getValue().toString();
                    }
                }

                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();

                fullnamme=fname+" "+lname;
                holder.textViewName.setText(fullnamme);
                holder.textViewdate.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, .class);
                intent.putExtra("Groupname",groupname);
                intent.putExtra("Num",num);
                context.startActivity(intent);
            }
        });*/
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
            textViewdate = itemView.findViewById(R.id.textview_date);
        }
    }
}
