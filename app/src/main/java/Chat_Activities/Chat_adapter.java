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
    private String userid,groupname;


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

        //holder.textViewName.setText(result.product);
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
              /*  amt=dataSnapshot.child("Groups").child(groupname).child("Chats").child(num).child("amount").getValue().toString();
              */
                date=dataSnapshot.child("Groups").child(groupname).child("Message").child(num).child("time").getValue().toString();
                /*email=dataSnapshot.child("Users").child(userid).child("Email").getValue().toString();
                gender=dataSnapshot.child("Users").child(userid).child("Gender").getValue().toString();
                dob=dataSnapshot.child("Users").child(userid).child("DOB").getValue().toString();
                phonenum=dataSnapshot.child("Users").child(userid).child("PhoneNumber").getValue().toString();
                if(dataSnapshot.child("Connection").child(uid).hasChild(userid))//childe(userid))
                {
                    status = dataSnapshot.child("Connection").child(uid).child(userid).getValue().toString();
                    Log.d("STATUS SEARCH ADAPTEE",status);
                }*/
                fullnamme=fname+" "+lname;
                holder.textViewName.setText(text);
                holder.textViewrupees.setText(fullnamme);
                holder.textViewdate.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat_details_Activity.class);
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

        TextView textViewName, textViewrupees,textViewdate;
        LinearLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            parentlayout = itemView.findViewById(R.id.search_parent);
            textViewrupees = itemView.findViewById(R.id.textview_by);
            textViewdate = itemView.findViewById(R.id.textview_date);

        }
    }
}
