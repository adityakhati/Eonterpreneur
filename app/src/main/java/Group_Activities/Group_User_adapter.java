package Group_Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.evineon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Group_User_adapter extends RecyclerView.Adapter<Group_User_adapter.ArtistViewHolder> {

    private Context context;
    private List<Group_Users> results;
    private EditText ed_search;
    private String fname, lname, email, dob, gender, phonenum, fullnamme, status, activity;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Group_User_adapter(Context mCtx, List<Group_Users> results, String activity) {
        context = mCtx;
        this.results = results;
        this.activity = activity;
    }


    @NonNull
    @Override
    public Group_User_adapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_user_group, parent, false);

        return new Group_User_adapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Group_User_adapter.ArtistViewHolder holder, final int position) {
        final Group_Users result = results.get(position);
        //holder.textViewName.setText(result.product);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String userid1 = result.uid;
        final String name = result.name;
        String email=result.email;

        holder.textViewName.setText(name);
        holder.textViewEmail.setText(email);
        /*holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("User")) {
                    Intent intent = new Intent(context, New_Profile_Activity.class);
                    intent.putExtra("Uid", userid1);
                    context.startActivity(intent);
                } else if (activity.equals("Group")) {
                    Intent intent = new Intent(context, Group_profile_Activity.class);
                    intent.putExtra("Name", name);
                    context.startActivity(intent);
                }
            }
        });
   */ }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewEmail;
        LinearLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.label);
            parentlayout = itemView.findViewById(R.id.linear_layout);
            textViewEmail = itemView.findViewById(R.id.label_1);

        }
    }
}