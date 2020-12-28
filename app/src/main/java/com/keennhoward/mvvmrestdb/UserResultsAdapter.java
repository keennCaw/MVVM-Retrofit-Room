package com.keennhoward.mvvmrestdb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.keennhoward.mvvmrestdb.model.Data;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;

public class UserResultsAdapter extends RecyclerView.Adapter<UserResultsAdapter.MyViewHolder> {

    private onItemClicked listener;
    private Context context;
    private List<Data> userList;

    public UserResultsAdapter(Context context, List<Data> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setUserList(List<Data> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = this.userList.get(position).getLast_name() + ", " + this.userList.get(position).getFirst_name();
        holder.idTextView.setText(String.valueOf(this.userList.get(position).getId()));
        holder.nameTextView.setText(name);
        holder.emailTextView.setText(this.userList.get(position).getEmail());

        Glide.with(context)
                .load(this.userList.get(position).getAvatar())
                //.apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatarImageView);
    }

    public Data getDataAt(int position){
        return userList.get(position);
    }

    @Override
    public int getItemCount() {
        if(this.userList != null){
            return this.userList.size();
        }

        return 0;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView avatarImageView;
        TextView emailTextView;
        TextView idTextView;
        ImageView addUserImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.user_item_email);
            avatarImageView = itemView.findViewById(R.id.user_item_avatar);
            nameTextView = itemView.findViewById(R.id.user_item_name);
            idTextView = itemView.findViewById(R.id.user_item_id);
            addUserImageView = itemView.findViewById(R.id.user_item_save_user);

            addUserImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(userList.get(position));
                    }
                }
            });
        }
    }
    public interface onItemClicked{
        void onItemClick(Data data);
    }

    public void setOnItemClickedListener(onItemClicked listener){this.listener = listener;}

}
