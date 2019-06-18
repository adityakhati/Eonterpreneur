package Group_Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    String activity;
    String groupname,usertype;

    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;


    public Group_User_adapter(Context mCtx, List<Group_Users> results, String activity,String groupname,String usertype) {
        context = mCtx;
        this.results = results;
        this.activity = activity;
        this.groupname=groupname;
        this.usertype=usertype;
    }



    @NonNull
    @Override
    public Group_User_adapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_user_group, parent, false);
        return new Group_User_adapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Group_User_adapter.ArtistViewHolder holder, final int position) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

            final Group_Users result = results.get(position);
            final String name = result.name;
            String email=result.email;

            holder.textViewName.setText(name);
            holder.textViewEmail.setText(email);

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usertype.equals("admin") || usertype.equals("superadmin")){
                    final Group_Users result = results.get(position);
                        final String[] listitems=new String[] {"admin","treasurer","secretary","new"};
                            AlertDialog.Builder mbuilder=new AlertDialog.Builder(context);
                            mbuilder.setTitle("Choose post:");
                            mbuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    myRef.child("Users").child(result.uid).child("usertype").setValue(listitems[i]);
                                    myRef.child("Groups").child(groupname).child("Users").child(result.uid).setValue(listitems[i]);
                                    dialog.dismiss();
                                }
                            });

                            mbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog mDialog=mbuilder.create();
                            mDialog.show();
                }
            }
        });
      }

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