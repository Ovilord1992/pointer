package com.pointer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pointer.DataModel.DataConverter;
import com.pointer.DataModel.User;

import java.util.List;

class UserRecycler extends RecyclerView.Adapter<UserViewHolder> {

    List<User> data;

    public UserRecycler (List<User> users){
        data = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.user_item_layout,
                viewGroup,
                false
        );
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        User user = data.get(i);
        userViewHolder.imageView.setImageBitmap(DataConverter.convertByteArray2BitmapImage(user.getImage()));
        userViewHolder.name.setText(user.getFullName());
        userViewHolder.dob.setText(String.valueOf(user.getDob()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
