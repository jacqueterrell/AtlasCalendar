package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.EditEmailLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;

import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_address_info.EditAddressFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
import javax.inject.Inject;

public class EditEmailFragment extends BaseFragment<EditEmailLayoutBinding,EditEmailViewModel> implements EditEmailNavigator {


    @Inject
    EditEmailDataModel dataModel;

    @Inject
    EditEmailViewModel viewModel;

    private EditEmailLayoutBinding binding;
    private static UserProfile profile;

    public static EditEmailFragment newInstance(UserProfile userProfile){

        profile = userProfile;
        return new EditEmailFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_email_layout;
    }

    @Override
    public EditEmailViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();

         setCachedData();

         return binding.getRoot();
    }

    @Override
    public void onContinueClicked() {

        setEmailData();

        ChangeFragments.addFragmentFadeIn(EditAddressFragment.newInstance(profile),getBaseActivity()
                .getSupportFragmentManager(),"AddressFragment",null);
    }

    @Override
    public void onSaveAndCloseClicked() {

        showProgressSpinner();
        setEmailData();
        viewModel.updateUser(getViewModel(),profile);
    }

    private void setEmailData(){

        String personalEmail = binding.etPersonalEmail.getText().toString();
        String workEmail = binding.etWorkEmail.getText().toString();
        Long timeStamp = System.currentTimeMillis() / 1000;

        profile.setTimestamp(timeStamp);
        profile.setEmail(personalEmail);
        profile.setWorkEmail(workEmail);
    }

    @Override
    public void onProfileUpdated() {

        showToastShort("Profile Updated");
        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        manager.popBackStack("UserProfile",0);
    }

    private void setCachedData(){

        binding.etPersonalEmail.setText(profile.getEmail());
        binding.etWorkEmail.setText(profile.getWorkEmail());
    }
}
