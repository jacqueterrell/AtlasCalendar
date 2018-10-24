package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.BusinessAccountsRecyclerViewBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivityNavigator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AdminAccountsFragment extends BaseFragment<BusinessAccountsRecyclerViewBinding, AdminAccountsViewModel>
        implements AdminAccountsNavigator {


    @Inject
    AdminAccountsViewModel viewModel;

    @Inject
    AdminAccountsDataModel dataModel;


    private BusinessAccountsRecyclerViewBinding binding;
    private List<UserProfile> adminProfiles = new ArrayList<>();
    private AdminAccountsAdapter adminAccountsAdapter;
    private WelcomeActivityNavigator parentNavigator;
    private FirebaseAuth firebaseAuth;


    public static AdminAccountsFragment newInstance(){

        return new AdminAccountsFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.business_accounts_recycler_view;
    }

    @Override
    public AdminAccountsViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (WelcomeActivityNavigator) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getViewDataBinding();

        adminAccountsAdapter = new AdminAccountsAdapter(adminProfiles, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adminAccountsAdapter);

        showProgressSpinner();

        if (firebaseAuth.getCurrentUser() == null){

            viewModel.signInAnonymously(getViewModel(),firebaseAuth);
            //showSnackbar("anonymously signing in...");

        } else {

            viewModel.getAllAdminProfiles(getViewModel());

        }

        return binding.getRoot();
    }

    @Override
    public void handleError(String errorMsg) {

        showAlert("Error",errorMsg);
        hideProgressSpinner();
    }

    @Override
    public void onAdminProfileSelected(UserProfile userProfile) {

        dataManager.getSharedPrefs().setUserId(userProfile.getId());
        parentNavigator.setBusinessLogin(false);
        openDashBoard();

    }

    @Override
    public void onAdminProfilesReturned() {

        adminProfiles.clear();
        adminProfiles.addAll(viewModel.getAdminProfileList());
        adminAccountsAdapter.notifyDataSetChanged();
        hideProgressSpinner();
    }

    @Override
    public void openDashBoard() {

        hideProgressSpinner();

        if (parentNavigator.isBusinessLogin()) {

            dataManager.getSharedPrefs().setBusinessAccount(true);
            getBaseActivity().finishAffinity();
            startActivity(DashBoardActivity.newIntent(getBaseActivity()));

        } else {

            dataManager.getSharedPrefs().setBusinessAccount(false);
            getBaseActivity().finishAffinity();
            startActivity(DashBoardActivity.newIntent(getBaseActivity()));

        }

    }
}
