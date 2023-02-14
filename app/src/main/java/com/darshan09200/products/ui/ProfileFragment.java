package com.darshan09200.products.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.darshan09200.products.MainActivity;
import com.darshan09200.products.databinding.FragmentProfileBinding;
import com.darshan09200.products.model.ProfileViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment implements MenuProvider {

    private FragmentProfileBinding binding;

    private ProfileViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            binding.name.setText(user.getDisplayName());
            binding.email.setText(user.getEmail());
        }
        binding.logout.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        AuthUI.getInstance()
                                .signOut(getActivity())
                                .addOnCompleteListener(task -> ((MainActivity) getActivity()).startSignIn());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        if (isVisible()) {
            menu.clear();
            System.out.println("called clear");
        }
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}