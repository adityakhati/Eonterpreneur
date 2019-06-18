package Account_Activities;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Accounts_adapter extends RecyclerView.Adapter<Accounts_adapter.ArtistViewHolder> {

    private Context context;
    private List<Account> results;
    private EditText ed_search;
    private String fname,lname,email,date,amt,phonenum,fullnamme,status;
    private String userid,groupname;


    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Accounts_adapter(Context mCtx, List<Account> results,String groupname) {
        context = mCtx;
        this.results = results;
        this.groupname=groupname;
    }


    @NonNull
    @Override
    public Accounts_adapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_accounts, parent, false);
        return new Accounts_adapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Accounts_adapter.ArtistViewHolder holder, final int position) {
        final Account result = results.get(position);
        //holder.textViewName.setText(result.product);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String num=result.num;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userid=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("madeby").getValue().toString();
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                amt=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("amount").getValue().toString();
                date=dataSnapshot.child("Groups").child(groupname).child("Accounts").child(num).child("date").getValue().toString();
                fullnamme=fname+" "+lname;
                holder.textViewName.setText(fullnamme);
                holder.textViewrupees.setText("Rs "+amt);
                holder.textViewdate.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Account_details_Activity.class);
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

        TextView textViewName, textViewrupees,textViewdate;
        LinearLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewrupees = itemView.findViewById(R.id.textview_rupees);
            textViewdate = itemView.findViewById(R.id.textview_date);

        }
    } 
}
