package com.example.dchatapplication.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchatapplication.ChatActivity;
import com.example.dchatapplication.HomeActivity;
import com.example.dchatapplication.Model.Users;
import com.example.dchatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
HomeActivity activity;
ArrayList<Users> usersArrayList;
    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
    this.activity=homeActivity;
    this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(activity).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users=usersArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
            holder.itemView.setVisibility(View.GONE);
        }
        holder.user_name.setText(users.getName());
        holder.user_status.setText(users.getStatus());
        Picasso.get().load(users.getImageUri()).into(holder.user_profile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReceiverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_profile=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }
}
