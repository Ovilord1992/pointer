package com.pointer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pointer.DataModel.UserDAO;
import com.pointer.DataModel.UserDatabase;

public class ShowUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        recyclerView = findViewById(R.id.userRecyclerView);

        userDAO = UserDatabase.getInstance(this).userDao();

        UserRecycler userRecycler = new UserRecycler(userDAO.getAllUsers());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userRecycler);

    }
}