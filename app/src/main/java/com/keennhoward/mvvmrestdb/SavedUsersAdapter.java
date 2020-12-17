package com.keennhoward.mvvmrestdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keennhoward.mvvmrestdb.room.User;

public class SavedUsersAdapter extends ListAdapter<User, SavedUsersAdapter.UserHolder> {

    private Context context;
    public SavedUsersAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getFirst_name().equals(newItem.getFirst_name()) && oldItem.getLast_name().equals(newItem.getLast_name())
                    && oldItem.getEmail().equals(newItem.getEmail()) && oldItem.getAvatar().equals(newItem.getAvatar());
        }
    };

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item,parent,false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = getItem(position);
        String name = currentUser.getLast_name() +", "+ currentUser.getFirst_name();
        holder.saveUserImageView.setVisibility(View.GONE);
        holder.nameTextView.setText(name);
        holder.emailTextView.setText(currentUser.getEmail());
        holder.idTextView.setText(String.valueOf(currentUser.getId()));
        Log.d("adapter", currentUser.toString());

        Glide.with(context)
                .load(currentUser.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatarImageView);
    }

    public User getUserAt(int position){
        return getItem(position);
    }


    class UserHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView;
        private TextView idTextView;
        private TextView emailTextView;
        private ImageView avatarImageView;
        private ImageView saveUserImageView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            saveUserImageView = itemView.findViewById(R.id.user_item_save_user);
            nameTextView = itemView.findViewById(R.id.user_item_name);
            idTextView = itemView.findViewById(R.id.user_item_id);
            emailTextView = itemView.findViewById(R.id.user_item_email);
            avatarImageView = itemView.findViewById(R.id.user_item_avatar);
        }
    }
}
