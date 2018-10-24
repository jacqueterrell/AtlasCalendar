package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.ContactInfoDialogBinding;

public class ContactInfoDialog extends Fragment {


    private ContactInfoDialogBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.contact_info_dialog,container,false);

        binding.dialogConnectionInfo.setOnClickListener(view -> {

            getActivity().onBackPressed();
        });

        return binding.getRoot();
    }
}
