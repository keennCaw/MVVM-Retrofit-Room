package com.keennhoward.mvvmrestdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.keennhoward.mvvmrestdb.model.Data;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Data> userList;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        final TextView noResultTextView = findViewById(R.id.noResultTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserResultsAdapter adapter = new UserResultsAdapter(this, userList);

        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

        userViewModel.getUserResponseLiveData().observe(this, new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> data) {
                if(data != null){
                    userList = data;
                    adapter.setUserList(userList);
                    noResultTextView.setVisibility(View.GONE);
                }else {
                    noResultTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        userViewModel.init();
    }
}
