package com.example.stomabooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.adapters.AssociatedUserAdapter;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton backIb;
    private TextView nameTv;
    private TextView emailTv;
    private TextView birthdateTv;
    private TextView emptyAssociatedUsersTv;
    private FloatingActionButton addAssociatedUserBtn;

    private final ArrayList<User> associatedUsersList = new ArrayList<>();
    private AssociatedUserAdapter associatedUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        setClickListeners();
        setData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddAssociatedAccountActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            int size = associatedUsersList.size();
            associatedUsersList.clear();
            associatedUserAdapter.notifyItemRangeRemoved(0, size);

            associatedUsersList.addAll(DatabaseManager.getInstance().getAllAssociatedUsers(UserManager.getInstance().getCurrentUser().id));
            associatedUserAdapter.notifyItemRangeInserted(0, associatedUsersList.size());
            emptyAssociatedUsersTv.setVisibility(associatedUsersList.size() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void initViews() {
        backIb = findViewById(R.id.ibBack);
        nameTv = findViewById(R.id.tvName);
        emailTv = findViewById(R.id.tvEmail);
        birthdateTv = findViewById(R.id.tvBirthdate);
        emptyAssociatedUsersTv = findViewById(R.id.tvEmptyAssociatedUsers);
        emptyAssociatedUsersTv.setVisibility(View.GONE);
        addAssociatedUserBtn = findViewById(R.id.fabAddAssociatedUser);

        RecyclerView associatedUsersRv = findViewById(R.id.rvAssociatedUsers);
        associatedUserAdapter = new AssociatedUserAdapter(associatedUsersList, (user, position) -> {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_associated_users_title))
                .setMessage(String.format(getString(R.string.delete_associated_users_message), user.firstName + " " + user.lastName))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.delete), (dialog, id) -> {
                    DatabaseManager.getInstance().removeUser(user);
                    associatedUsersList.remove(user);
                    associatedUserAdapter.notifyItemRemoved(position);
                    emptyAssociatedUsersTv.setVisibility(associatedUsersList.size() == 0 ? View.VISIBLE : View.GONE);
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, id) -> dialog.dismiss())
                .create().show();
        });
        associatedUsersRv.setAdapter(associatedUserAdapter);
    }

    private void setClickListeners() {
        backIb.setOnClickListener(v -> onBackPressed());
        addAssociatedUserBtn.setOnClickListener(v -> startActivityForResult(
            new Intent(this, AddAssociatedAccountActivity.class), AddAssociatedAccountActivity.REQUEST_CODE));
    }

    private void setData() {
        User user = UserManager.getInstance().getCurrentUser();
        nameTv.setText(user.firstName + " " + user.lastName);
        emailTv.setText(user.email);
        birthdateTv.setText(user.birthdate);

        associatedUsersList.addAll(DatabaseManager.getInstance().getAllAssociatedUsers(UserManager.getInstance().getCurrentUser().id));
        associatedUserAdapter.notifyItemRangeInserted(0, associatedUsersList.size());
        emptyAssociatedUsersTv.setVisibility(associatedUsersList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}