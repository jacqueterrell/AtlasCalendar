package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_first_name;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.EnterFirstNameDialogBinding;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_last_name.EnterLastNameFragment;
import com.team.mamba.atlascalendar.utils.ChangeWelcomeFragments;

public class EnterFirstNameFragment extends Fragment {


    private EnterFirstNameDialogBinding binding;
    private static long dateOfBirth;
    private LayoutInflater layoutInflater;
    private ViewGroup viewGroupContainer;

    public static EnterFirstNameFragment newInstance(long dob){

        dateOfBirth = dob;
        return new EnterFirstNameFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.enter_first_name_dialog,container,false);
        layoutInflater = inflater;
        viewGroupContainer = container;

        showSoftKeyboard(binding.etFirstName);


        binding.btnNext.setOnClickListener(v -> {

            String name = binding.etFirstName.getText().toString().trim();

            if (name.isEmpty()){

                Snackbar.make(binding.getRoot(),"Please enter a valid first name",Snackbar.LENGTH_LONG).show();

            } else {

                ChangeWelcomeFragments.addFragmentFadeIn(EnterLastNameFragment.newInstance(dateOfBirth,name), getActivity().getSupportFragmentManager(), "LastNameFragment", null);
            }
        });

        binding.layoutOpenWelcomeScreen.setOnClickListener(v -> {

            getActivity().onBackPressed();
        });

        if (!ChangeWelcomeFragments.isUnitTesting) {

            Glide.with(getActivity())
                    .load(R.drawable.welcome_background)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(binding.imgViewBackground);
        }

        return binding.getRoot();
    }

    private void showSoftKeyboard(View view) {

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public ViewGroup getViewGroupContainer() {
        return viewGroupContainer;
    }
}
