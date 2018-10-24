package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_employer;

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
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info.EditEmailFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import com.team.mamba.atlascalendar.databinding.EditEmployerPositionLayoutBinding;
import javax.inject.Inject;

public class EditEmployerFragment extends BaseFragment<EditEmployerPositionLayoutBinding,EditEmployerViewModel> implements EditEmployerNavigator{


    @Inject
    EditEmployerViewModel viewModel;

    @Inject
    EditEmployerDataModel dataModel;

    private static UserProfile profile;
    private EditEmployerPositionLayoutBinding binding;


    public static EditEmployerFragment newInstance(UserProfile userProfile){

        profile = userProfile;
        return new EditEmployerFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_employer_position_layout;
    }

    @Override
    public EditEmployerViewModel getViewModel() {
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

        setEmployerData();

        ChangeFragments.addFragmentFadeIn(EditEmailFragment.newInstance(profile),getBaseActivity()
                .getSupportFragmentManager(),"EmailFragment",null);
    }

    @Override
    public void onSaveAndCloseClicked() {

        showProgressSpinner();
        setEmployerData();
        viewModel.updateUser(getViewModel(),profile);
    }

    @Override
    public void onProfileUpdated() {

        showToastShort("Profile Updated");
        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        manager.popBackStack("UserProfile",0);
    }

    private void setEmployerData(){

        String position = binding.etTitle.getText().toString();
        String currentEmployer = binding.etWorkLocation.getText().toString();
        Long timeStamp = System.currentTimeMillis() / 1000;

        profile.setCurrentPosition(position);
        profile.setCurrentEmployer(currentEmployer);
        profile.setTimestamp(timeStamp);
    }

    private void setCachedData(){

        binding.etTitle.setText(profile.getCurrentPosition());
        binding.etWorkLocation.setText(profile.getCurrentEmployer());
    }
}
