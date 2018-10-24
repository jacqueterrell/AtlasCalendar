package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.AddUserLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsFragment;

import javax.inject.Inject;

public class AddUserFragment extends BaseFragment<AddUserLayoutBinding, AddUserViewModel>
implements AddUserNavigator {


    @Inject
    AddUserViewModel viewModel;

    private AddUserLayoutBinding binding;

    public static AddUserFragment newInstance(){

        return new AddUserFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_user_layout;
    }

    @Override
    public AddUserViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();


         return binding.getRoot();
    }

    @Override
    public void onNextButtonClicked() {

        String lastName = binding.etLastName.getText().toString();
        String userCode = binding.etUserCode.getText().toString();

        if (!viewModel.isLastNameValid(lastName) || !viewModel.isUserCodeValid(userCode)){

                String title = "Check Fields";
                String msg = "Please make sure that the name and code fields are complete";
                showAlert(title,msg);

        } else {

            ChangeFragments.addFragmentFadeIn(DescribeConnectionsFragment.newInstance(lastName,userCode),getBaseActivity()
                    .getSupportFragmentManager(),"AddUserLayout",null);

            //open next screen
        }
    }
}
