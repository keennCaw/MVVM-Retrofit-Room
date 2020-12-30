package com.keennhoward.mvvmrestdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.ArrayList;
import java.util.List;

public class SavedUsersAdapter extends ListAdapter<User, SavedUsersAdapter.UserHolder> implements Filterable {

    private OnItemClickListener listener;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    private List<User> userListFull;

    @Override
    public void submitList(@Nullable List<User> list) {
        super.submitList(list);
    }

    public void setAdapterList(List<User> list){
        userListFull = new ArrayList<>(list);
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) { //works on the background thread

            List<User> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(userListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(User user: userListFull){
                    if(user.getFirst_name().toLowerCase().startsWith(filterPattern)||user.getLast_name().toLowerCase().startsWith(filterPattern)||String.valueOf(user.getId()).startsWith(filterPattern)){
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values != null) {
                Log.d("result filter", "result"+ results.values.toString());
                submitList((List<User>) results.values);
                }else{

            }
            }
        };
    }
