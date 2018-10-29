package com.example.adeba.se_im.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adeba.se_im.R;
import com.example.adeba.se_im.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;
    private Context context;

    public UserListingRecyclerAdapter(List<User> users, Context context) {
        this.mUsers = users;
        this.context = context;

    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);

        holder.txtUsername.setText(user.displayName);
        Picasso.with(context)
                .load(user.displayPicture)
                .placeholder(R.drawable.user)
                .into(holder.displayPicture);
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUsername;
        private CircleImageView displayPicture;

        ViewHolder(View itemView) {
            super(itemView);
            displayPicture = (CircleImageView) itemView.findViewById(R.id.circular_image_view_user_dp);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
        }
    }
}
