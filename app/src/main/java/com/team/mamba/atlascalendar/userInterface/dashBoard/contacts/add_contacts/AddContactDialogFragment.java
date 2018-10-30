package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.app.ShareCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;

import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.AddContactsDialogBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user.AddUserFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts.SuggestedContactsFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

public class AddContactDialogFragment extends DialogFragment  {


    private AlertDialog dialog;
    private AddContactsDialogBinding binding;
    private DashBoardActivity parentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (DashBoardActivity) context;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.add_contacts_dialog,null,false);

        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setView(binding.getRoot());

        dialog = builder.create();
        dialog.show();

        binding.btnAddUser.setOnClickListener(view -> {

            ChangeFragments.addFragmentVertically(AddUserFragment.newInstance(),parentActivity.getSupportFragmentManager(),"AddUser",null);
            dialog.dismiss();
        });

        binding.btnAddBusiness.setOnClickListener(view -> {

            ChangeFragments.addFragmentVertically(AddBusinessFragment.newInstance(false),parentActivity.getSupportFragmentManager(),"AddBusiness",null);
            dialog.dismiss();
        });

        binding.btnInviteToAtlas.setOnClickListener(view -> {

            final String appPackageName = BuildConfig.APPLICATION_ID; // package name of the app
            String msg = "Join me on Atlas Networking! " + AppConstants.BASE_PLAY_STORE_LINK +  appPackageName;

            ShareCompat.IntentBuilder.from(parentActivity)
                    .setType("text/plain")
                    .setText(msg)
                    .startChooser();

            dialog.dismiss();
        });

        binding.btnFindUsers.setOnClickListener(view -> {

            ChangeFragments.addFragmentVertically(FindUsersFragment.newInstance(),parentActivity.getSupportFragmentManager(),"FindUsers",null);
            dialog.dismiss();
        });

        binding.btnSuggestedContacts.setOnClickListener(view -> {


            ChangeFragments.addFragmentVertically(SuggestedContactsFragment.newInstance(),parentActivity.getSupportFragmentManager(),"SuggestedContacts",null);
            dialog.dismiss();
        });

        binding.btnCancelAddContact.setOnClickListener(view -> {

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
