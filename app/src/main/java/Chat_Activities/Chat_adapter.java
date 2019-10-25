package Chat_Activities;

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

public class Chat_adapter extends RecyclerView.Adapter<Chat_adapter.ArtistViewHolder> {

    private Context context;
    private List<Chat> results;
    private EditText ed_search;
    private String fname,lname,email,date,amt,phonenum,fullnamme,status;
    private String userid,groupname,uid;


    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Chat_adapter(Context mCtx, List<Chat> results,String groupname) {
        context = mCtx;
        this.results = results;
        this.groupname=groupname;
    }


    @NonNull
    @Override
    public Chat_adapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_chat, parent, false);
        return new Chat_adapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Chat_adapter.ArtistViewHolder holder, final int position) {
        final Chat result = results.get(position);

        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        uid=user.getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String num=result.num;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userid=dataSnapshot.child("Groups").child(groupname).child("Message").child(num).child("By").getValue().toString();
                String text=dataSnapshot.child("Groups").child(groupname).child("Message").child(num).child("text").getValue().toString();
                fname=dataSnapshot.child("Users").child(userid).child("FirstName").getValue().toString();
                lname=dataSnapshot.child("Users").child(userid).child("LastName").getValue().toString();
                date=dataSnapshot.child("Groups").child(groupname).child("Message").child(num).child("time").getValue().toString();
                fullnamme=fname+" "+lname;
                if(userid.equals(uid))
                    holder.chatlayout.setBackgroundResource(R.drawable.bubble_in);
                else
                    holder.chatlayout.setBackgroundResource(R.drawable.bubble_out);
                holder.textViewName.setText(text);
                holder.textViewrupees.setText(fullnamme);
                holder.textViewdate.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewrupees,textViewdate;
        LinearLayout parentlayout,chatlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            chatlayout = itemView.findViewById(R.id.chat_layout);
            textViewrupees = itemView.findViewById(R.id.textview_by);
            textViewdate = itemView.findViewById(R.id.text_view_date);

        }
    }
}
