package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_last_name;

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
import com.team.mamba.atlascalendar.databinding.EnterLastNameDialogBinding;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number.EnterPhoneNumberFragment;
import com.team.mamba.atlascalendar.utils.ChangeWelcomeFragments;

public class EnterLastNameFragment extends Fragment {

    private EnterLastNameDialogBinding binding;
    private static String firstName;
    private static long dateOfBirth;


    public static EnterLastNameFragment newInstance(long dob,String name){

        dateOfBirth = dob;
        firstName = name;
        return new EnterLastNameFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.enter_last_name_dialog,container,false);

        showSoftKeyboard(binding.etLastName);

        binding.btnNext.setOnClickListener(v -> {

            String lastName = binding.etLastName.getText().toString().trim();

            if (lastName.isEmpty()){

                Snackbar.make(binding.getRoot(),"Please enter a valid last name",Snackbar.LENGTH_LONG).show();

            } else {

                //open enter phone fragment
                ChangeWelcomeFragments
                        .addFragmentFadeIn(EnterPhoneNumberFragment.newInstance(dateOfBirth,firstName,lastName), getActivity().getSupportFragmentManager(), "PhoneFragment", null);

            }
        });

        binding.layoutOpenFirstName.setOnClickListener(v -> {

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
