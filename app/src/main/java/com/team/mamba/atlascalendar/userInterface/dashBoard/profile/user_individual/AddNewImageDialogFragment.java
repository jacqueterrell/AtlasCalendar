package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;

import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.AddNewImageLayoutBinding;


public class AddNewImageDialogFragment extends DialogFragment {


    private static UserProfileNavigator navigator;
    private AddNewImageLayoutBinding binding;
    private AlertDialog dialog;



    public static AddNewImageDialogFragment newInstance(UserProfileNavigator userProfileNavigator){

        navigator = userProfileNavigator;
        return new AddNewImageDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.add_new_image_layout,null,false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        dialog = builder.create();
        dialog.show();

        binding.btnOpenLibrary.setOnClickListener(v -> {

            navigator.openGallery();
            dialog.dismiss();
        });

        binding.btnCamera.setOnClickListener(v -> {

            navigator.openCamera();
            dialog.dismiss();
        });

        binding.btnCancelAddContact.setOnClickListener(v -> {

            dialog.dismiss();
        });

        try {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }catch (Exception e){
            Logger.e(e.getMessage());
        }

        return dialog;
    }
}


