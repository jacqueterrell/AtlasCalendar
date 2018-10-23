package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.FindUsersLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;

import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecycler;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
import javax.inject.Inject;

public class FindUsersFragment extends BaseFragment<FindUsersLayoutBinding, FindUsersViewModel>
implements FindUsersNavigator {

    @Inject
    FindUsersViewModel viewModel;

    @Inject
    FindUsersDataModel dataModel;


    private FindUsersLayoutBinding binding;

    public static FindUsersFragment newInstance(){

        return new FindUsersFragment();
    }

    @Override
    public int getBindingVariable() {

        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.find_users_layout;
    }

    @Override
    public FindUsersViewModel getViewModel() {
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


         return binding.getRoot();
    }

    @Override
    public void onSearchButtonClicked() {

        String first = binding.etFirstName.getText().toString();
        String last = binding.etLastName.getText().toString();

        if (first.isEmpty() || last.isEmpty()){

            String title = "Check Fields";
            String msg = "Please make sure that the first name and last name fields are complete";
            showAlert(title,msg);

        } else {

            showProgressSpinner();
            viewModel.queryUsers(getViewModel(),first,last);
        }
    }

    @Override
    public void onUsersFound() {

        //set recyclerview
        hideProgressSpinner();
        ChangeFragments.addFragmentFadeIn(FindUsersRecycler.newInstance(viewModel.getQueriedProfiles()),getBaseActivity()
                .getSupportFragmentManager(),"FindUserRecycler",null);
    }



    @Override
    public void showUsersNotFoundAlert() {

        hideProgressSpinner();
        String first = binding.etFirstName.getText().toString();
        String last = binding.etLastName.getText().toString();

        String title = "User Not Found";
        String msg = "No user found with a first and last name of " + first + " " + last;
        showAlert(title,msg);
    }

}
