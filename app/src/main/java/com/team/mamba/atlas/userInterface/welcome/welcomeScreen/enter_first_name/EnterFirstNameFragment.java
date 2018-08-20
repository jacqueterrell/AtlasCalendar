package com.team.mamba.atlas.userInterface.welcome.welcomeScreen.enter_first_name;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.mamba.atlas.R;
import com.team.mamba.atlas.databinding.EnterFirstNameDialogBinding;
import com.team.mamba.atlas.userInterface.welcome.welcomeScreen.enter_last_name.EnterLastNameFragment;
import com.team.mamba.atlas.utils.ChangeFragments;
import com.team.mamba.atlas.utils.ChangeWelcomeFragments;

public class EnterFirstNameFragment extends Fragment {


    private EnterFirstNameDialogBinding binding;
    private static long dateOfBirth;

    public static EnterFirstNameFragment newInstance(long dob){

        dateOfBirth = dob;
        return new EnterFirstNameFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.enter_first_name_dialog,container,false);

        showSoftKeyboard(binding.etFirstName);

        binding.btnNext.setOnClickListener(v -> {

            String name = binding.etFirstName.getText().toString();

            if (name.isEmpty()){

                Snackbar.make(binding.getRoot(),"Please enter a valide first name",Snackbar.LENGTH_LONG).show();

            } else {

                ChangeWelcomeFragments
                        .addFragmentFadeIn(EnterLastNameFragment.newInstance(dateOfBirth,name), getActivity().getSupportFragmentManager(), "LastNameFragment", null);
            }
        });

        binding.layoutOpenWelcomeScreen.setOnClickListener(v -> {

            getActivity().onBackPressed();
        });

        Glide.with(getActivity())
                .load(R.drawable.welcome_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.imgViewBackground);

        return binding.getRoot();
    }

    private void showSoftKeyboard(View view) {

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }
}
