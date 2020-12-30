package com.keennhoward.mvvmrestdb.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.keennhoward.mvvmrestdb.R;
import com.keennhoward.mvvmrestdb.SavedUsersAdapter;
import com.keennhoward.mvvmrestdb.SavedUsersViewModel;
import com.keennhoward.mvvmrestdb.room.User;

import java.util.List;


public class SavedUsersFragment extends Fragment {

    private SavedUsersViewModel savedUsersViewModel;
    private SavedUsersAdapter savedUsersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_users, container, false);
        getActivity().setTitle("Saved Users");

        RecyclerView recyclerView = v.findViewById(R.id.saved_users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setHasFixedSize(true);

        savedUsersAdapter = new SavedUsersAdapter(v.getContext());
        recyclerView.setAdapter(savedUsersAdapter);

        savedUsersViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(SavedUsersViewModel.class);

        savedUsersViewModel.getAllUsers().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                savedUsersAdapter.submitList(users);
                savedUsersAdapter.setAdapterList(users);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                savedUsersViewModel.delete(savedUsersAdapter.getUserAt(viewHolder.getAdapterPosition()));
                getActivity().invalidateOptionsMenu();
                Toast.makeText(getContext(), "User Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        savedUsersAdapter.setOnItemClickListener(new SavedUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {

                SavedUsersFragmentDirections.NavigateToAddEditFragment action = SavedUsersFragmentDirections.navigateToAddEditFragment();
                action.setId(user.getId());
                action.setFirstName(user.getFirst_name());
                action.setLastName(user.getLast_name());
                action.setEmail(user.getEmail());
                action.setAvatar(user.getAvatar());

                Navigation.findNavController(v).navigate(action);
                Log.d("id", String.valueOf(user.getId()));
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.saved_users_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setIconifiedByDefault(false);
        Activity activity = getActivity();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TEST","rip");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                savedUsersAdapter.getFilter().filter(newText);
                return false;
            }

        });

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);


        menu.findItem(R.id.action_search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setQuery("",false);
                //on back pressed
                View view = activity.getCurrentFocus();
                if (view == null) {
                    view = new View(activity);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //add Delete All Users from DB
        switch (item.getItemId()){
            case R.id.delete_all_users:
                savedUsersViewModel.deleteAllUsers();
                Toast.makeText(getContext(), "All Users Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
