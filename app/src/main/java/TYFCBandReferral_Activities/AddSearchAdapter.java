package TYFCBandReferral_Activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class AddSearchAdapter extends RecyclerView.Adapter<AddSearchAdapter.ArtistViewHolder> {

private Context context;
private List<AddSearch> results;
private EditText ed_search;
private String fname,lname,email,dob,gender,phonenum,fullnamme,sending_uid,activity,grpname;

        DatabaseReference myRef;
        FirebaseDatabase mFirebaseDatabase;
        FirebaseAuth auth;


public AddSearchAdapter(Context mCtx, List<AddSearch> results,String activity,String grpname,String sending_uid) {
        context = mCtx;
        this.results = results;
        this.activity = activity;
        this.grpname=grpname;
        this.sending_uid=sending_uid;
        }


@NonNull
@Override
public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_parent_search_item, parent, false);

        return new ArtistViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final ArtistViewHolder holder, final int position) {
final AddSearch result = results.get(position);
        //holder.textViewName.setText(result.product);
        final String userid1=result.product;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userid=result.product;
                fname = dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname = dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                email = dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();

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
                    if(activity.equals("TYFCB")){
                        Intent intent = new Intent(context, TYFCB_Activity.class);
                        intent.putExtra("Uid", userid1);
                        intent.putExtra("Activity","add");
                        context.startActivity(intent);
                    }else if(activity.equals("ref")){
                        Intent intent = new Intent(context, Referral_Activity.class);
                        intent.putExtra("Uid", userid1);
                        intent.putExtra("Activity","add");
                        context.startActivity(intent);
                    }
                    else if(activity.equals("refof")){
                        Intent intent = new Intent(context, Referral_Activity.class);
                        intent.putExtra("Uid", userid1);
                        Log.d("Adapter UID",userid1);
                        intent.putExtra("Sending",sending_uid);
                        intent.putExtra("Activity","addref");
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
